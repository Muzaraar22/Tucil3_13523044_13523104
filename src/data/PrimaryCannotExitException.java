package src.data;

public class PrimaryCannotExitException extends InvalidBoardException {
    public PrimaryCannotExitException(){}
    public PrimaryCannotExitException(String s){
        super(s);
    }
}
