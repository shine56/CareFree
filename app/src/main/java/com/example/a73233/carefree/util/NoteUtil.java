package com.example.a73233.carefree.util;

import com.example.a73233.carefree.R;

public class NoteUtil{
    public static int GetColorByRank(int rank){
        switch (rank){
            case 0:
                return R.drawable.note_list_bg_0;
            case 1:
                return R.drawable.note_list_bg_1;
            case 2:
                return R.drawable.note_list_bg_2;
            case 3:
                return R.drawable.note_list_bg_3;
            default:
                return R.drawable.note_list_bg_0;
        }
    }
    public static int GetEnergy(int rank, int type){
        switch (rank){
            case 0:
                return 0;
            case 1:
                if(type == ConstantPool.COMPLETE) return 5;
                else return -5;
            case 2:
                if(type == ConstantPool.COMPLETE) return 10;
                else return -10;
            case 3:
                if(type == ConstantPool.COMPLETE) return 15;
                else return -15;
            default:
                return 0;
        }
    }
    public static int GetEnergyType(int rank){
        switch (rank){
            case 0:
                return 7;
            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 6;
            default:
                return 7;
        }
    }
}
