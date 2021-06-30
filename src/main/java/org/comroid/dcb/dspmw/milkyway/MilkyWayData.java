package org.comroid.dcb.dspmw.milkyway;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class MilkyWayData {
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

    @Override
    public String toString() {
        return String.format("MilkyWayData{totalGenCapMore=%d, totalGenCapLess=%d, totalSails=%d, totalPlayers=%d, totalSpheres=%d}",
                totalGenCapMore, totalGenCapLess, totalSails, totalPlayers, totalSpheres);
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
         */
    public static MilkyWayData read(byte[] data) {
        MilkyWayData it = new MilkyWayData();
        ByteBuffer bbuf;
        byte[] buf;

        bbuf = ByteBuffer.allocate(8);
        buf = Arrays.copyOfRange(data, 28, 35);
        for (int i = 0; i < buf.length; i++)
            bbuf.put(i, buf[i]);
        it.totalGenCapMore = bbuf.getLong();

        bbuf.clear();
        buf = Arrays.copyOfRange(data, 36, 43);
        for (int i = 0; i < buf.length; i++)
            bbuf.put(i, buf[i]);
        it.totalGenCapLess = bbuf.getLong();

        bbuf.clear();
        buf = Arrays.copyOfRange(data, 44, 51);
        for (int i = 0; i < buf.length; i++)
            bbuf.put(i, buf[i]);
        it.totalSails = bbuf.getLong();

        bbuf.clear();
        buf = Arrays.copyOfRange(data, 52, 55);
        for (int i = 0; i < buf.length; i++)
            bbuf.put(i, buf[i]);
        it.totalPlayers = bbuf.getInt();

        bbuf.clear();
        buf = Arrays.copyOfRange(data, 56, 59);
        for (int i = 0; i < buf.length; i++)
            bbuf.put(i, buf[i]);
        it.totalSpheres = bbuf.getInt();

        return it;
    }
}
