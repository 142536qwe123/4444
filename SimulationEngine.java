import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private final List<Person> people = new ArrayList<>();
    private int hour = 0;

    public void addPerson(Person p) {
        people.add(p);
    }

    // 每次 tick 代表 1 小时
    public void tick() {
        hour++;
        System.out.println("\n=== Hour " + hour + " ===");

        // 先让每个人执行他们的主动行为（这里示例：白天教师与学生各自可能 work）
        // 为了演示，我们在某些时段安排工作：9-12 以及 14-17
        int hofday = (hour - 1) % 24; // 0..23
        boolean isWorkTime = (hofday >= 9 && hofday <= 11) || (hofday >= 14 && hofday <= 16);

        for (Person p : people) {
            if (isWorkTime) {
                p.work();
            }
        }

        // 然后每个人被引擎 tick，自然衰减与自动行为触发
        for (Person p : people) {
            p.tick();
        }

        // 最后打印每个人的状态
        for (Person p : people) {
            System.out.println(p.toString());
        }
    }
}
