package org.comroid.dcb.dspmw.milkyway;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.util.Debug;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.Arrays;

public final class MilkyWayData {
    private static final Logger logger = LogManager.getLogger();
    private long totalGenCapMore;
    private long totalGenCapLess;
    private long totalSails;
    private int totalPlayers;
    private int totalSpheres;

    public long getTotalGenCapMore() {
        return totalGenCapMore;
    }

    public long getTotalGenCapLess() {
        return totalGenCapLess;
    }

    public long getTotalSails() {
        return totalSails;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public int getTotalSpheres() {
        return totalSpheres;
    }

    public String getPowerGen() {
        return powerGenToString(totalGenCapLess, totalGenCapMore);
    }

    public String getSailsCount() {
        return sailToString(totalSails);
    }

    private MilkyWayData() {
    }

    /*
        data loader:

        private bool InitClusterData(byte[] resData)
        {
            if (resData == null)
            {
                return false;
            }
            top10MinCap = 6E+10f;
            top100MinCap = 2E+10f;
            top1000MinCap = 6E+09f;
            int num = 0;
            BitConverter.ToInt32(resData, 0);
            BitConverter.ToInt32(resData, 4);
            int num2 = BitConverter.ToInt32(resData, 8);
            BitConverter.ToInt32(resData, 12);
            BitConverter.ToInt32(resData, 16);
            BitConverter.ToInt32(resData, 20);
            num += 24;
            BitConverter.ToInt32(resData, num);
            num += 4;
            totalGenCapMoreThanE = BitConverter.ToInt64(resData, num);
            totalGenCapLessThanE = BitConverter.ToInt64(resData, num + 8);
            totalSailLaunchedLessThanE = BitConverter.ToInt64(resData, num + 16);
            totalPlayer = BitConverter.ToInt32(resData, num + 24);
            totalDysonSphere = BitConverter.ToInt32(resData, num + 28);
            num += 32;


            ...


		arrowRt.eulerAngles = (descActive ? new Vector3(0f, 0f, -90f) : new Vector3(0f, 0f, 90f));
		if (tweener.normalizedTime > 0.8f && scrollToTarget)
		{
			double now = displayTotalDysonSphere;
			double now2 = displayTotalGenCapLessThanE;
			double now3 = displayTotalSailLaunchedLessThanE;
			now = Lerp.Tween(now, targetTotalDysonSphere, 6.0);
			now2 = Lerp.Tween(now2, targetTotalGenCapLessThanE, 16.0002);
			now3 = Lerp.Tween(now3, targetTotalSailLaunchedLessThanE, Localization.isCJK ? 4.9998000000000005 : 6.0);
			displayTotalDysonSphere = (long)Math.Ceiling(now);
			displayTotalGenCapLessThanE = (long)Math.Ceiling(now2);
			displayTotalSailLaunchedLessThanE = (long)Math.Ceiling(now3);
		}
		totalPlayerText.text = displayTotalDysonSphere.ToString("#,##0");
		totalGenCapsText.text = PowerGenToString(displayTotalGenCapLessThanE, displayTotalGenCapMoreThanE);
		totalsailLaunchedText.text = SailToString(displayTotalSailLaunchedLessThanE);
         */
    public static MilkyWayData read(byte[] data) {
        int dl = data.length;
        logger.info("Reversing MilkyWay data: " + Arrays.toString(Arrays.copyOfRange(data, 0, Math.min(dl, 64))));
        byte[] buf = new byte[dl];
        for (int i = 0; i < dl; i++)
            buf[i] = data[dl - i - 1];
        data = buf;

        MilkyWayData it = new MilkyWayData();
        ByteBuffer wrap;

        final int base = 24 + 4;
        logger.info("Reading MilkyWay data: " + Arrays.toString(Arrays.copyOfRange(data, 0, Math.min(dl, 64))));

        buf = Arrays.copyOfRange(data, base, base + 8);
        Debug.printByteArrayDump(Debug.logger, "totalGenCapMore", buf);
        wrap = ByteBuffer.wrap(buf);
        wrap.order(ByteOrder.BIG_ENDIAN);
        it.totalGenCapMore = wrap.getLong();

        buf = Arrays.copyOfRange(data, (base + 8), (base + 8) + 8);
        Debug.printByteArrayDump(Debug.logger, "totalGenCapLess", buf);
        wrap = ByteBuffer.wrap(buf);
        wrap.order(ByteOrder.BIG_ENDIAN);
        it.totalGenCapLess = wrap.getLong();

        buf = Arrays.copyOfRange(data, (base + 16), (base + 16) + 8);
        Debug.printByteArrayDump(Debug.logger, "totalSails", buf);
        wrap = ByteBuffer.wrap(buf);
        wrap.order(ByteOrder.BIG_ENDIAN);
        it.totalSails = wrap.getLong();

        buf = Arrays.copyOfRange(data, (base + 24), (base + 24) + 4);
        Debug.printByteArrayDump(Debug.logger, "totalPlayers", buf);
        wrap = ByteBuffer.wrap(buf);
        wrap.order(ByteOrder.BIG_ENDIAN);
        it.totalPlayers = wrap.getInt();

        buf = Arrays.copyOfRange(data, (base + 28), (base + 28) + 4);
        Debug.printByteArrayDump(Debug.logger, "totalSpheres", buf);
        wrap = ByteBuffer.wrap(buf);
        wrap.order(ByteOrder.BIG_ENDIAN);
        it.totalSpheres = wrap.getInt();

        return it;
    }

    @Override
    public String toString() {
        return String.format("MilkyWayData{" +
                        "\n\ttotalGenerated\t\t= %s," +
                        "\n\ttotalSails\t\t\t= %s," +
                        "\n\ttotalPlayers\t\t= %s," +
                        "\n\ttotalSpheres\t\t= %s" +
                        "\n}",
                getPowerGen(),
                getSailsCount(),
                new DecimalFormat().format(totalPlayers),
                new DecimalFormat().format(totalSpheres));
    }

    /*
  public string PowerGenToString(long low, long high) {
   high <= 0L ? low.ToString("#,##0") + " W" : string.Format("{0:#,##0},{1:000,000,000,000,000,000}", (object) high, (object) low) + " W";
  }
     */
    private String powerGenToString(long low, long high) {
        return (high <= 0L
                ? new DecimalFormat("#,##0").format(low)
                : new DecimalFormat("#,##0").format(high) + '.' + new DecimalFormat("000,000,000,000,000,000").format(low))
                + " W";
        /*
        if (high <= 0)
        {
            return String.format("%3d", low) + " W";
        }
        return Long.toString(high).substring(0, 4) + String.format("%18d", low) + " W";
         */
    }

    // ((double) count / 100000000.0).ToString("#,##0.0")
    private String sailToString(long count) {
        return new DecimalFormat("#,##0.0").format(count / 100_000_000.0);
        //return String.format("%4.0f", ((double)count / 1000000.0 - 0.5));
    }
}
