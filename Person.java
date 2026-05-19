public abstract class Person {
    private Name name;
    private int age;
    private Gender gender;
    private Role role;   // 新增：角色对象

    // Simulation state
    protected int energy;   // 0-100
    protected int hunger;   // 0-100
    protected int mood;     // 0-100

    private boolean isSleeping;
    private int sleepHoursRemaining;
    private int sleepCooldown; // ticks until auto-sleep allowed again

    protected int eatCooldown; // ticks until auto-eat allowed again

    // 无参构造函数
    public Person() {
        this.name = new Name();
        this.age = 0;
        this.gender = null;
        this.role = new Role(); // 默认为 Unknown 角色
        initSimDefaults();
    }

    // 兼容旧的子类构造：age, gender, name
    public Person(int age, Gender gender, Name name) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.role = new Role();
        initSimDefaults();
    }

    // 有参构造函数（增加 Role 参数）
    public Person(int age, Gender gender, Name name, Role role) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.role = role;
        initSimDefaults();
    }

    private void initSimDefaults() {
        this.energy = 100;
        this.hunger = 0;
        this.mood = 50;
        this.isSleeping = false;
        this.sleepHoursRemaining = 0;
        this.sleepCooldown = 0;
        this.eatCooldown = 0;
    }

    // 访问器
    public Name getName() { return name; }
    public void setName(Name name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // 修改后的 talk() 方法：委托给 role.roleTalk()
    public void talk() {
        if (role != null) {
            role.roleTalk();
        } else {
            System.out.println("No role assigned.");
        }
    }

    public void talk(String topic) {
        System.out.println("Let's talk about " + topic + ".");
    }

    public void chatWith(Person p, String topic) {
        String aName = this.name.toString();
        String bName = p.name.toString();
        System.out.println(aName + " to " + bName + ": Let's talk about " + topic + ".");
    }

    // 子类需实现具体的工作动作（打印/角色差异），并调用 doWork(...) 修改状态
    public abstract void work();

    // 每小时被 SimulationEngine 调用，处理自然衰减、睡眠、自动行为触发等
    public void tick() {
        String actor = name.toString();

        // 自然变化：饥饿
        int hungerInc = isSleeping ? 2 : 5;
        hunger = Math.min(100, hunger + hungerInc);

        // 能量变化
        if (isSleeping) {
            energy = Math.min(100, energy + 10);
            sleepHoursRemaining--;
            if (sleepHoursRemaining <= 0) {
                isSleeping = false;
                sleepCooldown = 1; // 至少一小时不再自动睡
                System.out.println(actor + " finished sleeping.");
            }
        } else {
            energy = Math.max(0, energy - 2);
        }

        // 冷却处理
        if (eatCooldown > 0) eatCooldown--;
        if (sleepCooldown > 0) sleepCooldown--;

        // 自动触发优先级：饥饿 > 精力
        if (hunger >= 80 && eatCooldown == 0) {
            System.out.println(actor + " is very hungry and will try to eat.");
            eat();
        } else if (energy <= 20 && !isSleeping && sleepCooldown == 0) {
            System.out.println(actor + " is very tired and will try to sleep for 1 hour.");
            sleep(1);
        }

        clampStats();
    }

    public final void breathe() {
        System.out.println("Breathing...");
    }

    public Person getPartner() {
        return null;
    }

    @Override
    public String toString() {
        return name.toString() + ", age=" + age + ", gender=" + gender + ", role=" + role.getRoleName()
                + ", E=" + energy + ", H=" + hunger + ", M=" + mood
                + (isSleeping ? " (sleeping)" : "");
    }

    // 行为实现帮助方法：吃、睡、工作影响器、社交
    public void eat() {
        if (eatCooldown > 0) {
            System.out.println(name + " is already recently eaten, skips eating.");
            return;
        }
        hunger = Math.max(0, hunger - 30);
        energy = Math.min(100, energy + 5);
        eatCooldown = 2; // 一段短冷却，避免连续自动吃
        System.out.println(name + " eats and recovers energy slightly.");
        clampStats();
    }

    public void sleep(int hours) {
        if (isSleeping) {
            System.out.println(name + " is already sleeping.");
            return;
        }
        isSleeping = true;
        sleepHoursRemaining = Math.max(1, hours);
        System.out.println(name + " starts sleeping for " + sleepHoursRemaining + " hour(s).");
    }

    protected void doWork(int energyCost, int hungerIncrease, int moodDelta) {
        energy = Math.max(0, energy - energyCost);
        hunger = Math.min(100, hunger + hungerIncrease);
        mood = Math.max(0, Math.min(100, mood + moodDelta));
        System.out.println(name + " worked: -E" + energyCost + " +H" + hungerIncrease + " M" + moodDelta);
        clampStats();
    }

    public void chatWith(Person other) {
        // 默认社交：双方心情+10，能量-2
        this.mood = Math.min(100, this.mood + 10);
        other.mood = Math.min(100, other.mood + 10);
        this.energy = Math.max(0, this.energy - 2);
        System.out.println(this.name + " chats with " + other.name + ". Mood up for both.");
        clampStats();
    }

    private void clampStats() {
        energy = Math.max(0, Math.min(100, energy));
        hunger = Math.max(0, Math.min(100, hunger));
        mood = Math.max(0, Math.min(100, mood));
    }
}