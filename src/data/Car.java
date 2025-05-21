package src.data;

import java.util.List;

public class Car {
    public char id;
    public List<Position> positions;
    public boolean isHorizontal;
    public boolean isPrimary;

    public Car(char id, List<Position> position, boolean isHorizontal, boolean isPrimary){
        this.id = id;
        this.positions = position;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Car{id=").append(id)
        .append(", isHorizontal=").append(isHorizontal)
        .append(", isPrimary=").append(isPrimary)
        .append(", positions=");

        // Menambahkan posisi dalam format [row, col] ke dalam string
        for (Position position : positions) {
            sb.append("[").append(position.row).append(",").append(position.col).append("] ");
        }
        
        sb.append("}");
        return sb.toString();
    }
}

    




    // public void move(int distance){
    //     if (this.type.equals("vertical")){
    //         for (int[] point : this.position){
    //             point[0] += distance;
    //         }   
    //     }else{
    //         for (int[] point : this.position){
    //             point[1] += distance;
    //         } 
    //     }
    // }

    // public void describeCar(){
    //     String msg = "name: " + this.name;
    //     msg +=  "\nposition: " + this.position.get(0)[1] + "," + this.position.get(0)[0]+ " - " + this.position.get(length-1)[1] + "," + this.position.get(length-1)[0];
    //     msg += "\ntype: " + this.type;
    //     System.out.println(msg);
    // }

    //driver
    // public static void main(String[] args) {
    ////   cek keamanan pointer
    //     ArrayList<int[]> point = new ArrayList<>();
    //     point.add(new int[]{1,4});
    //     point.add(new int[]{1,5});
    //     point.add(new int[]{1,6});
    //     Car a = new Car('A', 3, point, "vertical");
    //     Car b = new Car(a);

    //     a.describeCar();
    //     b.describeCar();
    //     b.move(3);
    //     a.describeCar();
    //     b.describeCar();
    // }
// }
