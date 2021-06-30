package org.comroid.dcb.dspmw.milkyway;

import org.comroid.api.BitmaskAttribute;
import org.comroid.dcb.dspmw.DspMilkyWayBot;
import org.comroid.util.Bitmask;
import org.comroid.util.Debug;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        return String.format("MilkyWayData{" +
                        "\n\ttotalGenerated\t\t= %s," +
                        "\n\ttotalSails\t\t\t= %s," +
                        "\n\ttotalPlayers\t\t= %d," +
                        "\n\ttotalSpheres\t\t= %d" +
                        "\n}",
                getPowerGen(), getSailsCount(), totalPlayers, totalSpheres);
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
        MilkyWayData it = new MilkyWayData();
        ByteBuffer wrap;
        byte[] buf;

        final int base = 0;

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

    public String getPowerGen() {
        return powerGenToString(totalGenCapLess, totalGenCapMore);
    }

    public String getSailsCount() {
        return sailToString(totalSails);
    }

    private String powerGenToString(long low, long high)
    {
        if (high <= 0)
        {
            return String.format("%3d", low) + " W";
        }
        return Long.toString(high).substring(0, 4) + String.format("%18d", low) + " W";
    }

    private String sailToString(long count)
    {
        return String.format("%4.0f", ((double)count / 1000000.0 - 0.5));
    }
}
