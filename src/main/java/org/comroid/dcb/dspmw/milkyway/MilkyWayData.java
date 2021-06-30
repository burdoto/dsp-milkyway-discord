package org.comroid.dcb.dspmw.milkyway;

import org.comroid.api.BitmaskAttribute;
import org.comroid.util.Bitmask;
import org.comroid.util.Debug;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
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
        return String.format("MilkyWayData{\n\ttotalGenCapMore\t=%d,\n\ttotalGenCapLess\t=%d,\n\ttotalSails\t\t=%d,\n\ttotalPlayers\t\t=%d,\n\ttotalSpheres\t\t=%d\n}",
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
        byte[] buf;

        buf = Arrays.copyOfRange(data, 28, 28 + 8);
        Debug.printByteArrayDump(Debug.logger, "totalGenCapMore", buf);
        it.totalGenCapMore = ByteBuffer.wrap(buf).getLong();

        buf = Arrays.copyOfRange(data, 36, 36 + 8);
        Debug.printByteArrayDump(Debug.logger, "totalGenCapLess", buf);
        it.totalGenCapLess = ByteBuffer.wrap(buf).getLong();

        buf = Arrays.copyOfRange(data, 44, 44 + 8);
        Debug.printByteArrayDump(Debug.logger, "totalSails", buf);
        it.totalSails = ByteBuffer.wrap(buf).getLong();

        buf = Arrays.copyOfRange(data, 52, 52 + 4);
        Debug.printByteArrayDump(Debug.logger, "totalPlayers", buf);
        it.totalPlayers = ByteBuffer.wrap(buf).getInt();

        buf = Arrays.copyOfRange(data, 56, 56 + 4);
        Debug.printByteArrayDump(Debug.logger, "totalSpheres", buf);
        it.totalSpheres = ByteBuffer.wrap(buf).getInt();

        return it;
    }
}
