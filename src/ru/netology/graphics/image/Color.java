package ru.netology.graphics.image;

public class Color implements TextColorSchema{
    @Override
    public char convert(int color) {
        //замена значения RGB пикселя на символ в зависимости от диапазона (0-255)
        char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '.'};
        return symbols[color / 32];
    }
}
