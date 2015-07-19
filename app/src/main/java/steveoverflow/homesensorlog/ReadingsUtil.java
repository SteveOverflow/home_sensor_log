package steveoverflow.homesensorlog;

/**
 * Created by stephentanton on 15-07-19.
 */
public class ReadingsUtil {
    public static String evaluateLightLevel(int lightVal){
        String level;

        if(lightVal<30){
            level="dark";
        }else if(lightVal<200){
            level="dim";
        }else if(lightVal<500){
            level="light";
        }else if(lightVal<800){
            level="bright";
        }else{
            level="very_bright";
        }

        return level;
    }
}
