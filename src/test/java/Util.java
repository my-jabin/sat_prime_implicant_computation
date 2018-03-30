package masterthesis.utils;

import masterthesis.base.ApplicationContext;
import masterthesis.base.Literal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static List<Literal> toLiteralsList(int[] values){
        ApplicationContext ac = ApplicationContext.getInstance();
        return Arrays.stream(values).mapToObj(ac::getLiteral).collect(Collectors.toList());
    }
}
