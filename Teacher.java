public class Teacher extends Person {
    private String subject;

    public Teacher(int age, Gender gender, Name name, String subject) {
        super(age, gender, name);
        this.subject = subject;
    }

    @Override
    public void talk() {
        System.out.println("Hi, how is your paper going?");
    }

    @Override
    public String toString() {
        return super.toString() + ", Subject: " + subject;
    }

    // 实现抽象方法 work
    @Override
    public void work() {
        System.out.println(getName() + " is teaching " + subject + ".");
        // 教师：能量消耗更多，但工作可能提升心情
        doWork(25, 5, +3);
    }

    @Override
    public void eat() {
        // 教师吃饭恢复饥饿相同，但精力恢复略少
        if (/* use eat cooldown in base */ false) {
            // unreachable placeholder to keep behavior consistent; base handles cooldown
        }
        // 调用基类逻辑 but reduce energy gain
        // 手动实现类似基类 eat 但精力恢复少一些
        // 注意：不能直接访问 base eatCooldown, 所以重implement
        // 减少 30 饥饿，精力恢复 +3
        this.hunger = Math.max(0, this.hunger - 30);
        this.energy = Math.min(100, this.energy + 3);
        this.eatCooldown = 2;
        System.out.println(getName() + " (teacher) eats and recovers a bit energy.");
    }
}