package practicing.asyncio.model;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

/**
 * @author: altenchen
 * @time: 2022/7/11
 * @description: 32960数据源封装
 */
@Data
@Builder
public class Source32960Model implements Serializable {

    /**
     * 消息id：
     * 消息的全局唯一标识
     */
    @NotEmpty(message = "messageId不可为空")
    private String messageId;
    /**
     * 车辆vin
     */
    @NotEmpty(message = "vin不可为空")
    private String vin;
    /**
     * 加密方式：
     * 1:数据不加密;
     * 2:数据经过 RSA 算法加密;
     * 3:数据经过 AES128位算法加密; 254表示异常, 255表示无效,其他预留
     */
    @EnumValue(intValues = {1, 2, 3}, message = "encryptType=[${validatedValue}], 超出取值范围")
    private int encryptType;
    /**
     * 数据采集时间
     */
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message="dataTime=[${validatedValue}], 时间格式不正确")
    private String dataTime;
    /**
     * 数据单元长度：后期合包可能会用到
     */
    @Digits(integer = 65531, fraction = 0, message = "length=[${validatedValue}], 超出大小限制")
    private int length;
    /**
     * 应答标志
     * 1: 成功，2：错误， 3：vin重复，254：表示数据包为命令包,而非应答包
     */
    @EnumValue(intValues = {1, 2, 3, 254}, message = "answerType=[${validatedValue}], 超出取值范围")
    private int answerType;
    /**
     * 校验结果：true: 通过, false: 失败
     */
    private boolean checkResult;
    /**
     * 命令标识
     * 1 车辆登入,
     * 2 实时信息上报,
     * 3 补发信息上报,
     * 4 车辆登出,
     * 5~6 平台传输数据占用
     * 7 心跳,
     * 8 终端校时,
     * 9~127, 数据系统预留,
     * 128 查询命令,
     * 129 设置命令,
     * 130 车载终端控制命令,
     * 131~191 下行数据系统预留,
     * 192~254 平台交换自定义数据
     */
    @Range(min = 1, max = 254, message = "commandType=[${validatedValue}], 超出阈值[{min}, {max}]")
    private int commandType;


    /***********************整车数据start***********************/
    /**
     * 车辆状态：
     * 1:车辆启动状态; 2:熄火; 3:其他状态; 254表 示异常, 255表示无效
     */
    @EnumValue(intValues = {1, 2, 3, 254, 255}, message = "vehicleState=[${validatedValue}], 超出取值范围")
    private int vehicleState;
    /**
     * 充电状态：
     * 1:停车充电; 2:行驶充电; 3:未充电状态; 4:充电完成; 254表示异常,255表示无效
     */
    @EnumValue(intValues = {1, 2, 3, 4, 254, 255}, message = "rechargeState=[${validatedValue}], 超出取值范围")
    private int rechargeState;
    /**
     * 运行模式：
     * 1:纯电; 2:混动; 3:燃油; 254表示异常; 255表示无效
     */
    @EnumValue(intValues = {1, 2, 3, 254, 255}, message = "runningMode=[${validatedValue}], 超出取值范围")
    private int runningMode;
    /**
     * 车速：
     * 有效值范围:0~220(表示0km/h~220km/h),最小计量 单元:0.1km/h, 65534表示异常, 65535表示无效
     */
    @ScopeOrSpecificValue(doubleMin = 0.0, doubleMax = 220.0, specificValue = {65534, 65535}, message = "speed=[${validatedValue}], 超出取值范围")
    private double speed;
    /**
     * 累计里程：
     * 有效值范围:0~999999.9(表示0km~999999.9km),最小 计量单元:0.1km。 4294967294 表示异常, 4294967295 表示无效
     */
    @ScopeOrSpecificValue(doubleMin = 0.0, doubleMax = 999999.9, specificValue = {4294967294L, 4294967295L}, message = "accumulativeMileage=[${validatedValue}], 超出取值范围")
    private double accumulativeMileage;
    /**
     * 总电压：
     * 有效值范围:0~1000(表示0V~1000V), 最小计量单元: 0.1V, 65534表示异常, 65535表示无效
     */
    @ScopeOrSpecificValue(doubleMin = 0.0, doubleMax = 1000.0, specificValue = {65534, 65535}, message = "totalVoltage=[${validatedValue}], 超出取值范围")
    private double totalVoltage;
    /**
     * 总电流：
     * 有效值范围:-1000~1000(表示-1000A~ +1000A),最小计量单元:0.1A, 65534表 示 异 常, 65535表示无效
     */
    @ScopeOrSpecificValue(doubleMin = -1000.0, doubleMax = 1000.0, specificValue = {65534, 65535}, message = "totalCurrent=[${validatedValue}], 超出取值范围")
    private double totalCurrent;
    /**
     * 档位：
     * 0:表示空挡, 1~6: 表示1挡~6挡, 13:倒挡 14:自动D挡 15:停车P挡
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 6, specificValue = {13, 14, 15}, message = "gear=[${validatedValue}], 超出取值范围")
    private int gear;
    /**
     * 是否有制动力：
     * 1:有制动力 0:无制动力
     */
    @EnumValue(intValues = {1, 0}, message = "brakingForce=[${validatedValue}], 超出取值范围")
    private int brakingForce;
    /**
     * 是否有驱动力：
     * 1:有驱动力 0:无驱动力
     */
    @EnumValue(intValues = {1, 0}, message = "drivingForce=[${validatedValue}], 超出取值范围")
    private int drivingForce;
    /**
     * 绝缘电阻：
     * 有效范围0~60000(表 示 0kΩ~60000kΩ), 最 小 计 量 单 元:1kΩ
     */
    @Range(min = 0, max = 60000, message = "insulationResistance=[${validatedValue}], 超出阈值[{min}, {max}]")
    private int insulationResistance;
    /**
     * 加速踏板行程值：
     * 有效值范围：0~100(表示0%~100%)，最小计量单元: 1%；“0”表示制动关的状态；在无具体行程值情况下，101：制动有效状态，254：异常，255：无效
     */
    @ScopeOrSpecificValue(intMin = 0, intMax = 100, specificValue = {101, 254, 255}, message = "accelerator=[${validatedValue}], 超出取值范围")
    private int accelerator;
    /**
     * 制动踏板状态：
     * 有效范围：0~60000(表示0kΩ~60000kΩ)，最小计量单元：1kΩ
     */
    @Range(min = 0, max = 60000, message = "brakePedalState=[${validatedValue}], 超出阈值[{min}, {max}]")
    private int brakePedalState;
    /**
     * SOC:
     * 有效值范围:0~100(表示0%~100%),最小计量单元:1%, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 0, intMax = 100, specificValue = {254, 255}, message = "soc=[${validatedValue}], 超出取值范围")
    private int soc;
    /**
     * dcdc状态：
     * 1:工作; 2:断开, 254表示异常, 255表示无效
     */
    @EnumValue(intValues = {1, 2, 254, 255}, message = "dcdcState=[${validatedValue}], 超出取值范围")
    private int dcdcState;

    /***********************整车数据end***********************/

    /***********************驱动电机start***********************/
    /**
     * 驱动电机个数：
     * 有效值1~253
     */
    @Range(min = 1, max = 253, message = "drivingMotorCount=[${validatedValue}], 超出阈值[{min}, {max}]")
    private int drivingMotorCount;
    /**
     * 驱动电机总成信息列表:
     * <motorSeq_itemName, mapping_value>
     *
     * 驱动电机序号           drivingMotorSerialNumber
     * 驱动电机状态           drivingMotorState
     * 驱动电机控制器温度 	    drivingMotorControllerTemp
     * 驱动电机转速 	        drivingMotorRpm
     * 驱动电机转矩 	        drivingMotorTorque
     * 驱动电机温度 	        drivingMotorTemp
     * 电机控制器输入电压 	    motorControllerInputVoltage
     * 电机控制器直流母线电流 	motorControllerIDcCurrent
     */
    private List<DrivingMotorModel> drivingMotorAssemblyInfoList;

    /***********************驱动电机end***********************/

    /***********************车辆位置数据start***********************/
    /**
     * 定位状态：
     * 0:有效定位;
     * 1:无效定位(当数据通信正常,而不能获取定位信息时,发送最后一次有效定位信息,并将定位状态置为无效。)
     */
    @EnumValue(intValues = {0, 1}, message = "positioningState=[${validatedValue}], 超出取值范围")
    private int positioningState;
    /**
     * 纬度状态：
     * 0:北纬;1:南纬
     */
    @EnumValue(intValues = {0, 1}, message = "lat=[${validatedValue}], 超出取值范围")
    private int lat;
    /**
     * 经度状态：
     * 0:东经;1:西经
     */
    @EnumValue(intValues = {0, 1}, message = "lon=[${validatedValue}], 超出取值范围")
    private int lon;
    /**
     * 中国境内：
     * 最东端 东经135度2分30秒 黑龙江和乌苏里江交汇处
     * 最西端 东经73度40分 帕米尔高原乌兹别里山口（乌恰县）
     * 最南端 北纬3度52分 南沙群岛曾母暗沙
     * 最北端 北纬53度33分 漠河以北黑龙江主航道（漠河)
     *
     * 经度：
     * 格式: xx.xxxxxx, 如：117.359423
     */
    @ScopeOrSpecificValue(doubleMin = 73.666667, doubleMax = 135.041667, message = "longitude=[${validatedValue}], 超出取值超出阈值[{doubleMin}, {doubleMax}]范围")
    private double longitude;
    /**
     * 纬度：
     * 格式: xx.xxxxxx, 如：39.066203
     */
    @ScopeOrSpecificValue(doubleMin = 3.883333, doubleMax = 53.550000, message = "latitude=[${validatedValue}], 超出取值超出阈值[{doubleMin}, {doubleMax}]范围")
    private double latitude;

    /***********************车辆位置数据end***********************/

    /***********************极值数据start***********************/
    /**
     * 最高电压电池子系统号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "maxVoltageBatterySubsystemNumber=[${validatedValue}], 超出取值范围")
    private int maxVoltageBatterySubsystemNumber;
    /**
     * 最高电压电池单体代号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "maxVoltageBatteryCode=[${validatedValue}], 超出取值范围")
    private int maxVoltageBatteryCode;
    /**
     * 电池单体电压最高值:
     * 有效值范围:0~15(表示0V~15V),最小计量单元: 0.001V, 65534表示异常, 65535表示无效
     */
    @ScopeOrSpecificValue(doubleMin = 0, doubleMax = 15, specificValue = {65534, 65535}, message = "maxVoltageBatteryValue=[${validatedValue}], 超出取值范围")
    private double maxVoltageBatteryValue;
    /**
     * 最低电压电池子系统号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "minVoltageBatterySubsystemNumber=[${validatedValue}], 超出取值范围")
    private int minVoltageBatterySubsystemNumber;
    /**
     * 最低电压电池单体代号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "minVoltageBatteryCode=[${validatedValue}], 超出取值范围")
    private int minVoltageBatteryCode;
    /**
     * 电池单体电压最低值:
     * 有效值范围:0~15(表示0V~15V),最小计量单元: 0.001V, 65534表示异常, 65535表示无效
     */
    @ScopeOrSpecificValue(doubleMin = 0, doubleMax = 15, specificValue = {65534, 65535}, message = "minVoltageBatteryValue=[${validatedValue}], 超出取值范围")
    private double minVoltageBatteryValue;
    /**
     * 最高温度子系统号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "maxTempSubsystemNumber=[${validatedValue}], 超出取值范围")
    private int maxTempSubsystemNumber;
    /**
     * 最高温度探针序号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "maxTempProbeNumber=[${validatedValue}], 超出取值范围")
    private int maxTempProbeNumber;
    /**
     * 最高温度值:
     * 有效值范围:-40~210(表 示 -40 ℃ ~ +210 ℃),最小计量单元:1 ℃, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = -40, intMax = 210, specificValue = {254, 255}, message = "maxTempValue=[${validatedValue}], 超出取值范围")
    private int maxTempValue;
    /**
     * 最低温度子系统号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "minTempSubsystemNumber=[${validatedValue}], 超出取值范围")
    private int minTempSubsystemNumber;
    /**
     * 最低温度探针序号:
     * 有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "minTempProbeNumber=[${validatedValue}], 超出取值范围")
    private int minTempProbeNumber;
    /**
     * 最低温度值:
     * 有效值范围:-40~210(表 示 -40 ℃ ~ +210 ℃),最小计量单元:1 ℃, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = -40, intMax = 210, specificValue = {254, 255}, message = "minTempValue=[${validatedValue}], 超出取值范围")
    private int minTempValue;

    /***********************极值数据end***********************/

    /***********************报警数据start***********************/
    /**
     * 最高报警等级：
     * 为当前发生的故障中的最高等级值,有效值范围:0~3,
     * “0” 表示无故障;
     * “1”表示1级故障,指代不影响车辆正常行驶的 故障;
     * “2”表示2级故障,指代影响车辆性能,需驾驶员限制行 驶的故障;
     * “3”表示3级故障,为最高级别故障,指代驾驶员应 立即停车处理或请求救援的故障;
     * 具体等级对应的故障内容 由厂商自行定义;
     * 254 表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 0, intMax = 3, specificValue = {254, 255}, message = "maxAlarmLevel=[${validatedValue}], 超出取值范围")
    private int maxAlarmLevel;
    /**
     * 温度差异报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "temperatureDeviation=[${validatedValue}], 超出取值范围")
    private int temperatureDeviation;
    /**
     * 电池高温报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "batteryHighTemperature=[${validatedValue}], 超出取值范围")
    private int batteryHighTemperature;
    /**
     * 车载储能装置类型过压报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "energyStorageOvervoltage=[${validatedValue}], 超出取值范围")
    private int energyStorageOvervoltage;
    /**
     * 车载储能装置类型欠压报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "energyStorageUndervoltage=[${validatedValue}], 超出取值范围")
    private int energyStorageUndervoltage;
    /**
     * SOC低报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "socLow=[${validatedValue}], 超出取值范围")
    private int socLow;
    /**
     * 单体电池过压报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "batteryOvervoltage=[${validatedValue}], 超出取值范围")
    private int batteryOvervoltage;
    /**
     * 单体电池欠压报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "batteryUndervoltage=[${validatedValue}], 超出取值范围")
    private int batteryUndervoltage;
    /**
     * SOC过高报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "socTooHigh=[${validatedValue}], 超出取值范围")
    private int socTooHigh;
    /**
     * SOC跳变报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "socLeaping=[${validatedValue}], 超出取值范围")
    private int socLeaping;
    /**
     * 可充电储能系统不匹配报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "rechargeableDeviceMismatching=[${validatedValue}], 超出取值范围")
    private int rechargeableDeviceMismatching;
    /**
     * 电池单体一致性差报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "batteryConsistencyPoor=[${validatedValue}], 超出取值范围")
    private int batteryConsistencyPoor;
    /**
     * 绝缘报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "insulation=[${validatedValue}], 超出取值范围")
    private int insulation;
    /**
     * DC-DC温度报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "dcdcTemperature=[${validatedValue}], 超出取值范围")
    private int dcdcTemperature;
    /**
     * 制动系统报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "brakeSystem=[${validatedValue}], 超出取值范围")
    private int brakeSystem;
    /**
     * DC-DC状态报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "dcdcStateAlarm=[${validatedValue}], 超出取值范围")
    private int dcdcStateAlarm;
    /**
     * 驱动电机控制器温度报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "motorControllerTemperature=[${validatedValue}], 超出取值范围")
    private int motorControllerTemperature;
    /**
     * 高压互锁状态报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "highVoltageInterlock=[${validatedValue}], 超出取值范围")
    private int highVoltageInterlock;
    /**
     * 驱动电机温度报警:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "motorTemperature=[${validatedValue}], 超出取值范围")
    private int motorTemperature;
    /**
     * 车载储能装置类型过充:
     * 1:报警；0:正常
     */
    @EnumValue(intValues = {0, 1}, message = "energyStorageOvercharge=[${validatedValue}], 超出取值范围")
    private int energyStorageOvercharge;
    /**
     * 可充电储能装置故障总数 N1:
     * N1个可充电储能装置故障,有效值范围:0~252, 254表 示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 0, intMax = 252, specificValue = {254, 255}, message = "rechargeableDeviceFaultCountN1=[${validatedValue}], 超出取值范围")
    private int rechargeableDeviceFaultCountN1;
    /**
     * 可充电储能装置故障代码列表:
     * 扩展性数据,由厂商自行定义,可充电储能装置故障个数等 于可充电储能装置故障总数 N1,eg:[11111,2222,3333,4444]
     */
    private int[] rechargeableDeviceFaultCodeList;
    /**
     * 驱动电机故障总数 N2:
     * N2 个驱动电机故障,有效值范围:0~252, 254表 示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 0, intMax = 252, specificValue = {254, 255}, message = "drivingMotorFaultTotalCountN2=[${validatedValue}], 超出取值范围")
    private int drivingMotorFaultTotalCountN2;
    /**
     * 驱动电机故障代码列表：
     * 厂商自行定义,驱动电机故障个数等于驱动电机故障总数N2,eg:[11111,2222,3333,4444]
     */
    private int[] drivingMotorFaultCodeList;
    /**
     * 发动机故障总数：
     * N3个驱动电机故障,有效值范围:0~252, 254表 示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 0, intMax = 252, specificValue = {254, 255}, message = "engineFaultTotalCountN3=[${validatedValue}], 超出取值范围")
    private int engineFaultTotalCountN3;
    /**
     * 发动机故障列表：
     * 厂商自行定义,发动机故障个数等于驱动电机故障总数 N3,eg:[11111,2222,3333,4444]
     */
    private int[] engineFaultList;
    /**
     * 其他故障总数：
     * N4个其他故障,有效值范围:0~252, 254表 示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 0, intMax = 252, specificValue = {254, 255}, message = "otherFaultTotalCountN4=[${validatedValue}], 超出取值范围")
    private int otherFaultTotalCountN4;
    /**
     * 其他故障代码列表：
     * 厂商自行定义,故障个数等于故障总数 N4,eg:[11111,2222,3333,4444]
     */
    private int[] otherFaultCodeList;


    /***********************报警数据end***********************/

    /***********************可充电储能装置电压数据start***********************/
    /**
     * 可充电储能子系统个数:
     * N个可充电储能子系统,有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "rechargeableDeviceSystemvoltageCount=[${validatedValue}], 超出取值范围")
    private int rechargeableDeviceSystemVoltageCount;
    /**
     * 可充电储能子系统电压信息列表:
     * <subSysNumber_itemName, mapping_value>
     *
     * 可充电储能子系统号		number
     * 可充电储能装置电压	    batteryVoltage
     * 可充电储能装置电流	    batteryCurrent
     * 单体电池总数	        cellSum
     * 本帧起始电池序号	    frameStartCellSeq
     * 本帧单体电池总数(m)	    frameCellSum
     * 单体电池电压	        cellVoltages
     */
    private List<RechargeableDeviceSystemVoltageModel> voltageInfoList;


    /***********************可充电储能装置电压数据end***********************/

    /***********************可充电储能装置温度数据start***********************/
    /**
     * 可充电储能子系统个数：
     * N个可充电储能装置,有效值范围:1~250, 254表示异常, 255表示无效
     */
    @ScopeOrSpecificValue(intMin = 1, intMax = 250, specificValue = {254, 255}, message = "rechargeableDeviceSystemTempCount=[${validatedValue}], 超出取值范围")
    private int rechargeableDeviceSystemTempCount;
    /**
     * 可充电储能子系统温度信息列表:
     * 可充电储能子系统号		                number
     * 可充电储能温度探针个数		            probeSum
     * 可充电储能子系统各温度探针检测到的温度值	batteryTemperatures
     */
    private List<RechargeableDeviceSystemTempModel> tempInfoList;


    /***********************可充电储能装置温度数据end***********************/

}
