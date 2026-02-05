package org.firstinspires.ftc.teamcode.FSL.helper;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Scoring{

    public static final LinkedList<Color> NONE = new LinkedList<Color>(List.of());
    public static final LinkedList<Color> G = new LinkedList<Color>(List.of(Color.GREEN));
    public static final LinkedList<Color> P = new LinkedList<Color>(List.of(Color.PURPLE));
    public static final LinkedList<Color> PPG = new LinkedList<Color>(List.of(Color.PURPLE, Color.PURPLE, Color.GREEN));
    public static final LinkedList<Color> PGP = new LinkedList<Color>(List.of(Color.PURPLE, Color.GREEN, Color.PURPLE));
    public static final LinkedList<Color> GPP = new LinkedList<Color>(List.of(Color.GREEN, Color.PURPLE, Color.PURPLE));

    public static LinkedList<Color> convertToScoringPattern(Motif m){
        switch (m){
            case GPP:
                return GPP;
            case PGP:
                return PGP;
            case PPG:
                return PPG;
        }
        return GPP;
    }
}
