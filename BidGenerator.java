
public class BidGenerator {

    public OpenClose fileManager = new OpenClose();
    public int hours;
    public int id;
    public int bids;
    int bidNumber;
    public int tolerance = 10;
    public double weeklyPay;
    public double arrayLength;
    public double arraySize;
    public double weeklyAvg;
    double weeklyHours;
    int restart;
    int shift1, shift2, shift3, shift4;
    int weeklyTotal;
    String d1, d2, d3, d4, st1, st2, st3, st4;
    int shift1Day, shift2Day, shift3Day, shift4Day, checkDay;
    boolean check;
    double tipsAverage;
    int usableBids=0;

    String shifts[][] = new String[100][9];
    String bidSchedule[][] = new String[100][6];

    public void fillArray() {
        id = 0;
        fileManager.openReadBidsFile();
        try {
            for (int row = 0; row < shifts.length; row++) {
                shifts[row][0] = Integer.toString(id);
                shifts[row][1] = fileManager.shiftReader.next();
                shifts[row][2] = fileManager.shiftReader.next();
                shifts[row][3] = fileManager.shiftReader.next();
                shifts[row][4] = fileManager.shiftReader.next();
                shifts[row][5] = fileManager.shiftReader.next();
                shifts[row][7] = fileManager.shiftReader.next();
                shifts[row][6] = fileManager.shiftReader.next();
                shifts[row][8] = fileManager.shiftReader.next();
                fileManager.shiftReader.nextLine();
                id++;
            }
        } catch (Exception e) {
        }
        fileManager.closeReadFile();
        arraySize = id - 1;
    }

    public double setTipsAverage(){
        String tips[][] = new String[100][9];
        id = 0;
        fileManager.openReadBidsFile();
        try {
            for (int row = 0; row < shifts.length; row++) {
                tips[row][0] = Integer.toString(id);
                tips[row][1] = fileManager.shiftReader.next();
                tips[row][2] = fileManager.shiftReader.next();
                tips[row][3] = fileManager.shiftReader.next();
                tips[row][4] = fileManager.shiftReader.next();
                tips[row][5] = fileManager.shiftReader.next();
                tips[row][7] = fileManager.shiftReader.next();
                tips[row][6] = fileManager.shiftReader.next();
                tips[row][8] = fileManager.shiftReader.next();
                fileManager.shiftReader.nextLine();
                id++;
            }
        } catch (Exception e) {
        }
        fileManager.closeReadFile();
        arraySize = id - 1;
        tipsAverage=0;
        int count=0;
        for (int row = 0; row < arraySize; row++) {
            if (tips[row][8].equals("Available")) {
                tipsAverage += Double.parseDouble(tips[row][6]);
                count++;
            }
        }
        tipsAverage = (tipsAverage/count*4);
        usableBids = count;
        return tipsAverage;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean newCheck) {
        check = newCheck;
    }

    public double getArrayLength() {
        return arrayLength;
    }

    public void setArrayLength() {
        this.arrayLength = bids;
    }

    public void printTable() {
        for (int row = 0; row < shifts.length; row++) {
            for (int column = 0; column < shifts[row].length; column++) {
                System.out.print(shifts[row][column] + "\t");
            }
            System.out.println();
        }
    }


    public int getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = (tolerance / 2);
    }

    public void generateBidSchedule(int numberOfBids, int threeDay) {
        BidMakerMain buttonFunctions = new BidMakerMain();
        setTipsAverage();
        bids = numberOfBids;
        int threeDayOff = threeDay;

        resetGenerator();
        bidNumber = 0;
        restart = 0;

        if (bids<=(usableBids/4)) {
            for (int i = 0; i < threeDayOff; i++) {
                System.out.println(i);
                makeThreeDayOffBid();
                weeklyTotal = (Integer.parseInt(shifts[shift1][6]) + Integer.parseInt(shifts[shift2][6]) + Integer.parseInt(shifts[shift3][6]) + Integer.parseInt(shifts[shift4][6]));
                weeklyHours = (Double.parseDouble(shifts[shift1][5]) + Double.parseDouble(shifts[shift2][5]) + Double.parseDouble(shifts[shift3][5]) + Double.parseDouble(shifts[shift4][5]));
                bidSchedule[bidNumber][0] = Integer.toString(shift1);
                bidSchedule[bidNumber][1] = Integer.toString(shift2);
                bidSchedule[bidNumber][2] = Integer.toString(shift3);
                bidSchedule[bidNumber][3] = Integer.toString(shift4);
                bidSchedule[bidNumber][4] = Integer.toString(weeklyTotal);
                bidSchedule[bidNumber][5] = Double.toString(weeklyHours);
                bidNumber++;
            }
            while (bidNumber < numberOfBids) {
                makeBid();

                weeklyTotal = (Integer.parseInt(shifts[shift1][6]) + Integer.parseInt(shifts[shift2][6]) + Integer.parseInt(shifts[shift3][6]) + Integer.parseInt(shifts[shift4][6]));
                weeklyHours = (Double.parseDouble(shifts[shift1][5]) + Double.parseDouble(shifts[shift2][5]) + Double.parseDouble(shifts[shift3][5]) + Double.parseDouble(shifts[shift4][5]));
                bidSchedule[bidNumber][0] = Integer.toString(shift1);
                bidSchedule[bidNumber][1] = Integer.toString(shift2);
                bidSchedule[bidNumber][2] = Integer.toString(shift3);
                bidSchedule[bidNumber][3] = Integer.toString(shift4);
                bidSchedule[bidNumber][4] = Integer.toString(weeklyTotal);
                bidSchedule[bidNumber][5] = Double.toString(weeklyHours);
                bidNumber++;
            }
        }
    }

    public void printBidSchedule(int numberOfBids) {
        for (int i = 0; i < numberOfBids; i++) {
            System.out.print(bidSchedule[i][0] + "\t");
            System.out.print(bidSchedule[i][1] + "\t");
            System.out.print(bidSchedule[i][2] + "\t");
            System.out.print(bidSchedule[i][3] + "\t");
            System.out.print(bidSchedule[i][4] + "\t");
            System.out.print(bidSchedule[i][5] + "\t");
            System.out.println();
        }
    }

    public void resetGenerator() {
        for (int row = 0; row < arraySize; row++) {
            if (shifts[row][8] == "Taken" || shifts[row][8] == "null") {
                shifts[row][8] = "Available";
            }
        }
    }

    public int setDay(String day) {
        int dow = 0;
        switch (day) {
            case "Monday":
                dow = 1;
                break;
            case "Tuesday":
                dow = 2;
                break;
            case "Wednesday":
                dow = 3;
                break;
            case "Thursday":
                dow = 4;
                break;
            case "Friday":
                dow = 5;
                break;
            case "Saturday":
                dow = 6;
                break;
            case "Sunday":
                dow = 7;
                break;
        }
        return dow;
    }

    public int checkDay() {
        int x = 0;
        shift1Day = setDay(d1);
        shift2Day = setDay(d2);
        shift3Day = setDay(d3);
        shift4Day = setDay(d4);

        if (Math.abs(shift1Day - shift2Day) == 1 || Math.abs(shift1Day - shift2Day) == 6) {
            x++;
        }
        if (Math.abs(shift2Day - shift3Day) == 1 || Math.abs(shift2Day - shift3Day) == 6) {
            x++;
        }
        if (Math.abs(shift3Day - shift4Day) == 1 || Math.abs(shift3Day - shift4Day) == 6) {
            x++;
        }
        if (Math.abs(shift4Day - shift1Day) == 1 || Math.abs(shift4Day - shift1Day) == 6) {
            x++;
        }
        if (Math.abs(shift1Day - shift3Day) == 1 || Math.abs(shift1Day - shift3Day) == 6) {
            x++;
        }
        if (Math.abs(shift2Day - shift4Day) == 1 || Math.abs(shift2Day - shift4Day) == 6) {
            x++;

        }

        checkDay = x;

        return checkDay;
    }

    public void makeBid() {
        int high = (int) tipsAverage + getTolerance();
        int low = (int) tipsAverage - getTolerance();
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int twoDays = 0;

        try {
            for (i = 0; i < arraySize; i++) {
                for (j = 0; j < arraySize; j++) {
                    for (k = 0; k < arraySize; k++) {
                        for (l = 0; l < arraySize; l++) {
                            d1 = shifts[i][1];
                            d2 = shifts[j][1];
                            d3 = shifts[k][1];
                            d4 = shifts[l][1];
                            st1 = shifts[i][7];
                            st2 = shifts[j][7];
                            st3 = shifts[k][7];
                            st4 = shifts[l][7];
                            if (!(d1.equals(d2)) &&
                                    !(d1.equals(d3)) &&
                                    !(d1.equals(d4)) &&
                                    !(d2.equals(d3)) &&
                                    !(d2.equals(d4)) &&
                                    !(d3.equals(d4)) &&
                                    (st1.equals(st2)) &&
                                    (st1.equals(st3)) &&
                                    (st1.equals(st4)) &&
                                    (shifts[i][8].equals("Available")) &&
                                    (shifts[j][8].equals("Available")) &&
                                    (shifts[k][8].equals("Available")) &&
                                    (shifts[l][8].equals("Available"))) {
                                twoDays = checkDay();
                                if (!((Double.parseDouble(shifts[i][5]) + Double.parseDouble(shifts[j][5]) + Double.parseDouble(shifts[k][5]) + Double.parseDouble(shifts[l][5])) < 32) &&
                                        twoDays == 2) {
                                    if (!((Integer.parseInt(shifts[i][6]) + Integer.parseInt(shifts[j][6]) + Integer.parseInt(shifts[k][6]) + Integer.parseInt(shifts[l][6])) < low) &&
                                            !((Integer.parseInt(shifts[i][6]) + Integer.parseInt(shifts[j][6]) + Integer.parseInt(shifts[k][6]) + Integer.parseInt(shifts[l][6])) > high)) {
                                        shift1 = i;
                                        shift2 = j;
                                        shift3 = k;
                                        shift4 = l;
                                        shifts[shift1][8] = "Taken";
                                        shifts[shift2][8] = "Taken";
                                        shifts[shift3][8] = "Taken";
                                        shifts[shift4][8] = "Taken";
                                        return;
                                    }
                                }

                            }

                        }
                    }
                }
            }
            if (i == (int) arraySize &&
                    j == (int) arraySize &&
                    k == (int) arraySize &&
                    l == (int) arraySize) {
                BidMakerMain buttonFunctions = new BidMakerMain();
                check = false;
            }

        } catch (StackOverflowError e) {
            BidMakerMain buttonFunctions = new BidMakerMain();
            check = false;
        }
    }

    public void makeThreeDayOffBid() {
        int high = (int) tipsAverage + getTolerance();
        int low = (int) tipsAverage - getTolerance();
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int threeDays = 0;

        try{
            for (i = 0; i < arraySize; i++) {
                for (j = 0; j < arraySize; j++) {
                    for (k = 0; k < arraySize; k++) {
                        for (l = 0; l < arraySize; l++) {
                            d1 = shifts[i][1];
                            d2 = shifts[j][1];
                            d3 = shifts[k][1];
                            d4 = shifts[l][1];
                            st1 = shifts[i][7];
                            st2 = shifts[j][7];
                            st3 = shifts[k][7];
                            st4 = shifts[l][7];
                            if (!(weeklyTotal < low) &&
                                    !(weeklyTotal > high) &&
                                    !(d1.equals(d2)) &&
                                    !(d1.equals(d3)) &&
                                    !(d1.equals(d4)) &&
                                    !(d2.equals(d3)) &&
                                    !(d2.equals(d4)) &&
                                    !(d3.equals(d4)) &&
                                    (st1.equals(st2)) &&
                                    (st1.equals(st3)) &&
                                    (st1.equals(st4)) &&
                                    (shifts[i][8].equals("Available")) &&
                                    (shifts[j][8].equals("Available")) &&
                                    (shifts[k][8].equals("Available")) &&
                                    (shifts[l][8].equals("Available"))) {
                                threeDays = checkDay();
                                if (!((Double.parseDouble(shifts[i][5]) + Double.parseDouble(shifts[j][5]) + Double.parseDouble(shifts[k][5]) + Double.parseDouble(shifts[l][5])) < 32)&&
                                        threeDays == 3) {
                                    if(!((Integer.parseInt(shifts[i][6]) + Integer.parseInt(shifts[j][6]) + Integer.parseInt(shifts[k][6]) + Integer.parseInt(shifts[l][6])) < low) &&
                                            !((Integer.parseInt(shifts[i][6]) + Integer.parseInt(shifts[j][6]) + Integer.parseInt(shifts[k][6]) + Integer.parseInt(shifts[l][6])) < low)) {
                                        shift1 = i;
                                        shift2 = j;
                                        shift3 = k;
                                        shift4 = l;
                                        shifts[shift1][8] = "Taken";
                                        shifts[shift2][8] = "Taken";
                                        shifts[shift3][8] = "Taken";
                                        shifts[shift4][8] = "Taken";

                                        return;
                                    }
                                }
                            }

                        }
                    }
                }
            }
            if(i == (int)arraySize &&
                    j == (int)arraySize &&
                    k == (int)arraySize &&
                    l == (int)arraySize){
                BidMakerMain buttonFunctions = new BidMakerMain();
                check = false;
            }

        }
        catch(StackOverflowError e) {
            BidMakerMain buttonFunctions = new BidMakerMain();
            check = false;
        }
    }
}



