package org.comroid.dcb.dspmw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.common.io.FileHandle;
import org.comroid.dcb.dspmw.milkyway.MilkyWayData;
import org.comroid.dcb.dspmw.milkyway.endpoint.MwEndpoint;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.util.ReaderUtil;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DspMilkyWayBot implements MessageCreateListener {
    public static final String ERROR_RESPONSE = "Error:500";
    public static final FileHandle DIR = new FileHandle("/srv/dcb/dsp/mwv/", true);
    public static final Logger logger = LogManager.getLogger();
    public static DspMilkyWayBot instance;
    private final FileHandle dir;
    private final FileHandle token;
    private final FileHandle steamId;
    private final FileHandle lastUpdate;
    private final FileHandle cacheFile;
    private final REST rest;
    private final DiscordApi discord;
    private final long loginKey = 0;

    public DspMilkyWayBot(FileHandle dir) {
        this.dir = dir;
        this.token = dir.createSubFile("discord.txt");
        this.steamId = dir.createSubFile("steamId.txt");
        this.lastUpdate = dir.createSubFile("lastupdate.txt");
        this.cacheFile = dir.createSubFile("cache.bin");
        this.rest = new REST();
        this.discord = new DiscordApiBuilder()
                .setToken(token.getContent())
                .addListener(this)
                .login()
                .join();
    }

    public static void main(String[] args) {
        try {
            instance = new DspMilkyWayBot(DIR);
        } catch (Exception e) {
            logger.fatal("Could not start up Bot", e);
        }
    }

    private CompletableFuture<Long> requestLoginKey() {
        return rest.request()
                .method(REST.Method.GET)
                .endpoint(MwEndpoint.LOGIN_HEADER, steamId.getContent(true))
                .execute()
                .thenApply(response -> {
                    Reader reader = response.getData().orElseGet(ReaderUtil::empty);
                    byte[] data = ReaderUtil.toArray(reader);
                    String str = new String(data);
                    logger.info("Received login data: " + str);

                    // check for error data // fixme
                    if (data.length == 0 || data[0] == 'E') {
                        logger.warn("Login key request failed! Data: " + str);
                        return 0L; // login key request failed
                    }

                    String[] split = str.split(",");
                    if (split.length != 2 || !split[1].matches("\\d+"))
                        return 0L;
                    return Long.parseLong(split[1]);
                });
    }

    private CompletableFuture<MilkyWayData> requestFullData() {
        if (!lastUpdate.isEmpty() && Long.parseLong(lastUpdate.getContent()) + TimeUnit.HOURS.toMillis(3) > System.currentTimeMillis())
            return CompletableFuture.completedFuture(MilkyWayData.read(cacheFile.getContent().getBytes()));
        logger.info("Updating data cache...");
        return requestLoginKey().thenCompose(loginKey -> rest.request()
                .method(REST.Method.GET)
                .endpoint(MwEndpoint.DOWNLOAD, loginKey)
                .addHeader(CommonHeaderNames.ACCEPTED_CONTENT_TYPE, "galaxy/fulldata")
                .execute()
                .thenApply(response -> {
                    Reader reader = response.getData().orElseGet(ReaderUtil::empty);
                    byte[] data = ReaderUtil.toArray(reader);
                    String str = new String(data);
                    try (Writer w = cacheFile.openWriter()) {
                        w.write(str);
                    } catch (IOException e) {
                        logger.error("Could not write data cache", e);
                    } finally {
                        lastUpdate.setContent("" + System.currentTimeMillis());
                    }
                    logger.info("Received MilkyWay data: " + (str.length() < 300 ? str : "(data too long)"));

                    // check for error data // fixme
                    if (data.length == 0 || data[0] == 'E') {
                        logger.warn("Download request failed! Data: " + str);
                        return null; // download request failed
                    }
                    return MilkyWayData.read(data);
                }));
    }

    @Override
    public void onMessageCreate(final MessageCreateEvent event) {
        if (event.getMessageAuthor().isYourself())
            return;
        if (!event.getMessageAuthor().isBotOwner())
            return;
        if (!event.getMessageContent().startsWith("!milkyway"))
            return;
        String str;
        CompletableFuture.supplyAsync(() -> sendMilkyWayData(event));
    }

    private Void sendMilkyWayData(MessageCreateEvent event) {
        String str;
        try {
            MilkyWayData data = requestFullData().join();
            str = data == null ? "Could not retrieve Milky Way data" : ("```" + data + "```");
        } catch (Throwable e) {
            StringWriter ex = new StringWriter();
            PrintWriter pw = new PrintWriter(ex);
            e.printStackTrace(pw);
            str = "Could not parse Milky Way data: " + ex;
            pw.close();
        }
        logger.info("Data: " + str);
        event.getChannel().sendMessage(str);
        return null;
    }
}