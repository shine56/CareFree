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
}
