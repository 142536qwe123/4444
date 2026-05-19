public class Student extends Person {
    private String major;

    public Student(int age, Gender gender, Name name, String major) {
        super(age, gender, name);
        this.major = major;
    }

    @Override
    public void talk() {
        System.out.println("Hi, how is your homework going?");
    }

    @Override
    public String toString() {
        return super.toString() + ", Major: " + major;
    }

    // 实现抽象方法 work
    @Override
    public void work() {
        System.out.println(getName() + " is studying " + major + ".");
        // 学生：能量消耗较小，饥饿增加中等，心情略有下降
        doWork(15, 5, -2);
    }

    @Override
    public void chatWith(Person other) {
        // 学生社交收益更高
        this.mood = Math.min(100, this.mood + 15);
        other.mood = Math.min(100, other.mood + 10);
        this.energy = Math.max(0, this.energy - 2);
        System.out.println(getName() + " chats happily with " + other.getName() + ".");
    }
}