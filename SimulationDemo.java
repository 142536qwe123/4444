public class SimulationDemo {
    public static void main(String[] args) {
        SimulationEngine engine = new SimulationEngine();

        Name sName = new Name("Alice", "Johnson");
        Name tName = new Name("Bob", "Smith");

        Student stu = new Student(20, Gender.FEMALE, sName, "Computer Science");
        Teacher tea = new Teacher(35, Gender.MALE, tName, "Mathematics");

        engine.addPerson(stu);
        engine.addPerson(tea);

        // 人为地让学生稍微更累/饿，便于观察自动行为
        stu.doWork(30, 20, -5); // simulate a heavy morning study before simulation

        // 运行 24 小时
        for (int i = 1; i <= 24; i++) {
            engine.tick();

            // 中午时段安排社交（示例）
            if (i == 12) {
                stu.chatWith(tea);
            }
        }
    }
}
