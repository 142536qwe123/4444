/**
 * 日志分析工具类：通过迭代器访问序列并执行统计/分析。
 */
public class LogAnalyzer {

    /**
     * 统计指定级别的日志条目数。仅通过迭代器访问序列元素。
     * 只认为完全符合格式 "LEVEL|timestamp|message" 的记录有效，且 LEVEL 必须为 INFO/WARN/ERROR。
     *
     * @param logSeq 日志序列
     * @param level  要统计的级别（INFO、WARN、ERROR）
     * @return 匹配的日志条目数
     */
    public static int countByLevel(Sequence logSeq, String level) {
        if (logSeq == null || level == null) return 0;
        if (!"INFO".equals(level) && !"WARN".equals(level) && !"ERROR".equals(level)) return 0;

        int count = 0;
        SeqIterator it = logSeq.iterator();
        while (it.hasNext()) {
            SequenceItem item = it.next();
            if (item == null) continue;
            String data = item.getData();
            if (data == null) continue;

            String[] parts = data.split("\\|", -1);
            if (parts.length < 3) continue; // 需要 LEVEL|timestamp|message

            String lvl = parts[0];
            if (!level.equals(lvl)) continue;

            // 校验 timestamp 为非负整数
            String tsStr = parts[1];
            try {
                long ts = Long.parseLong(tsStr);
                if (ts < 0) continue;
            } catch (NumberFormatException e) {
                continue;
            }

            // 符合条件，计数
            count++;
        }

        return count;
    }

}
