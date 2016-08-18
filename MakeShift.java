
public class MakeShift extends BidMakerMain{

    public String day, bar, shiftType, available;
    public int inTime, outTime, tips, rank;
    double hours;
    public Double shiftGrade;

    public MakeShift(int number) {
        this.rank = number+1;
        this.day = "";
        this.bar = "";
        this.inTime = 0000;
        this.outTime = 0000;
        this.hours = 000;
        this.shiftType = "";
        this.tips = 000;
        this.shiftGrade = Math.random();
        this.available = "Available";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public int getInTime() {
        return inTime;
    }

    public void setInTime(int inTime) {
        this.inTime = inTime;
    }

    public int getOutTime() {
        return outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        double in = inTime;
        double out = outTime;
        if(out < in){
            this.hours = Math.ceil((out - in + 2400)/100);
        }
        else {
            this.hours = Math.ceil((out - in)/100);
        }
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType() {
        if (inTime<1200) {
            this.shiftType = "AM";
        }
        else{
            this.shiftType = "PM";
        }
    }

    public int getTips() {
        return tips;
    }

    public void setTips(int tips) {
        this.tips = tips;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String availability) {
        this.available = availability;
    }

    public Double getShiftGrade() {
        return shiftGrade;
    }

}