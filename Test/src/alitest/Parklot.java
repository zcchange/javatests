//评测题目: 某停车场(Parklot)有停车位(ParkingSpace)若干：
//-有一个入口和一个出口，入口完成扫描计时，出口完成结账及车位释放。
//-停车位包含两类：货车位和小车位，货车按每小时10元计价，每天最高累计120元，小车位按每小时5元计价，每天最高累计60元。
//-请注意提示剩余车位信息
//为该停车场设计一个管理系统，还原该场景，
//功能包括：（1）车辆进入处理（2）车辆离开处理（3）计算当日停车场缴费总金额 等。
package alitest;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
//评测题目: 某停车场(Parklot)有停车位(ParkingSpace)若干：
//-有一个入口和一个出口，入口完成扫描计时，出口完成结账及车位释放。
//-停车位包含两类：货车位和小车位，货车按每小时10元计价，每天最高累计120元，小车位按每小时5元计价，每天最高累计60元。
//-请注意提示剩余车位信息
//为该停车场设计一个管理系统，还原该场景，
//功能包括：（1）车辆进入处理（2）车辆离开处理（3）计算当日停车场缴费总金额 等。
//评测题目: 某停车场(Parklot)有停车位(ParkingSpace)若干：
//-有一个入口和一个出口，入口完成扫描计时，出口完成结账及车位释放。
//-停车位包含两类：货车位和小车位，货车按每小时10元计价，每天最高累计120元，小车位按每小时5元计价，每天最高累计60元。
//-请注意提示剩余车位信息
//为该停车场设计一个管理系统，还原该场景，
//功能包括：（1）车辆进入处理（2）车辆离开处理（3）计算当日停车场缴费总金额 等。
/**
 * @author zhengcheng
 * 考察：面向对象设计、多线程操作方法设计
 */
public class Parklot {

    //车位总数
    private LongAdder parkingSpaces = new LongAdder();

    //
    private Map<CarType, LongAdder> mapSpace = new HashMap<>(8);

    //当前库存车对应的车辆类型库存，KEY为license，初始化大小适应业务
    private ConcurrentHashMap<String,CarType> carTypes = new ConcurrentHashMap<>(16);


    //进入的车辆时间汇总,KEY同样为license，value为进入时间（直接用LocalDateTime好像更合适）
    private ConcurrentHashMap<String,Instant> carsInTime = new ConcurrentHashMap<>(16);

    //当日入账总金额.初始化为0。由于只有一个出口。不存在缴费并发
    //private volatile int totalInMoney;
    private LongAdder totalInMoney = new LongAdder();

    //重置的同时，若发生车辆出库
    // private ReentrantLock resetLock = new ReentrantLock();



    //需要有个定时任务每天凌晨重置当天的计费，重置计费与出口缴费存在冲突
//    private ScheduledFuture<?> scheduleFuture;



    /**
     * 初始化停车位
     * @param spaceNum
     */
    public Parklot(int spaceNum) {
        this.parkingSpaces.add(spaceNum);
    }


    /**
     *
     * @param spaceOfCar
     * @param spaceOftrunk
     */
    public Parklot(int spaceOfCar,int spaceOftrunk) {
        this(spaceOfCar + spaceOftrunk);

        LongAdder adderCar = new LongAdder();
        LongAdder adderTrunk = new LongAdder();

        adderCar.add(spaceOfCar);
        adderTrunk.add(spaceOftrunk);

        this.mapSpace.put(CarType.CAR,adderCar);
        this.mapSpace.put(CarType.TRUCK,adderTrunk);



    }


    /**
     * 车辆进入处理方法
     * @param carType 车子类型
     * @param licence 车牌号
     * @return
     */
    public boolean carIn(CarType carType,String licence) {
        if(reserveSpace(carType) <= 0) {
            System.out.println(carType.name + "停车位不足");
            return false;
        }

        System.out.println("当前的剩余" + carType.name + "停车位：" + reserveSpace(carType));
//        parkingSpaces.decrement();
        spaceDecrement(carType);
        carTypes.put(licence,carType);
        carsInTime.put(licence,Instant.now());
        return true;

    }


    /**
     * 车辆出处理方法，需要算出车辆的开销
     * @param licence
     * @return
     */
    public boolean carOut(String licence) {

        //挪车
        CarType removedCar = carTypes.get(licence);
        carTypes.remove(licence);
        //入场时间
        Instant instantIn = carsInTime.get(licence);
        carsInTime.remove(licence);
        //计费,先算小时计费,注意下  向上 取整。。。。。。
        long seconds = Duration.between(instantIn,Instant.now()).getSeconds();
        long costHour =(long)Math.ceil(seconds / 3600d);
        long cost = removedCar.unitPrice * costHour;
        if (cost > removedCar.upPrice) {
            cost = removedCar.upPrice;
        }
        //
        totalInMoney.add(cost);
        //车位
//        parkingSpaces.increment();
        spaceAdd(removedCar);
        return true;

    }


    /**
     * 剩余停车位
     * @return
     */
    public int reserveSpace() {
        return parkingSpaces.intValue();
    }

    /**
     * 剩余停车位
     * 针对车子类型的
     * @return
     */
    public int reserveSpace(CarType carType) {
        return  this.mapSpace.get(carType).intValue();
    }


    /**
     *返回当前当天的收入
     * @return
     */
    public int totalIn() {
        return totalInMoney.intValue();
    }

    //
    private void spaceAdd(CarType carType) {
        this.mapSpace.get(carType).increment();
        this.parkingSpaces.increment();
    }


    private void spaceDecrement(CarType carType) {
        this.mapSpace.get(carType).decrement();
        this.parkingSpaces.decrement();
    }


    /**
     * 凌晨重置当天收入
     */
    public void resetTodayMoney() {
        //上锁
        //
        totalInMoney.reset();
    }


    /**
     * 车辆类型
     * 参数包括每小时计价以及当天最高累计金额
     */
    public enum CarType {
        CAR("小车",5,60),
        TRUCK("卡车",10,120);


        private CarType(String name,int u, int t) {
            this.name = name;
            this.unitPrice = u;
            this.upPrice = t;
        }

        //收费单价
        private int unitPrice;
        //收费单日上线
        private int upPrice;

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getUpPrice() {
            return upPrice;
        }

        public void setUpPrice(int upPrice) {
            this.upPrice = upPrice;
        }
    }

}
