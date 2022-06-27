package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Image implements TextGraphicsConverter {

    int maxWidth;
    int maxHeight;
    double maxRatio;
    int width = 1;
    int height = 1;
    double ratio;
    int newHeight;
    int newWidth;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        //считываем изображение из ссылки в буфер
        BufferedImage img = ImageIO.read(new URL(url));

        // берем высоту и ширину изображения, рассчитываем соотношение сторон
        height = img.getHeight();
        width = img.getWidth();
        ratio = (double) height / width;

        //при диапазоне выше заданного выкидываем исключение
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        //приводим размер к максимально возможному, если изначальный размер превышает разрешенный
        // если высота превышает допустимое значение
        if (height > maxHeight) {
            newHeight = maxHeight;
            newWidth =  (int) (newHeight / ratio);
        } else {
            newHeight = height;
            newWidth = width;
        }

        //если после приведения высоты, ширина все еще больше допустимого значения
        if (newWidth > maxWidth) {
            int coefficient = newWidth / maxWidth;
            newWidth = maxWidth;
            newHeight = newHeight / coefficient;
        }

        // берем изображение с измененными размерами
        java.awt.Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        //создаем объект с чернобелым изображением
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        //вызываем инструмент рисования
        Graphics2D graphics = bwImg.createGraphics();
        //копируем уменьшенную картинку в объект
        graphics.drawImage(scaledImage, 0, 0, null);

        //инструмент для прохода по пикселям
        WritableRaster bwRaster = bwImg.getRaster();

        //создаем массив для хранения значений RGB
        char[][] pixels = new char[newHeight][newWidth];
        Color schema = new Color();

        //в цикле проходим по изображению и сохраняем значения RGB пикселей в массив
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];
                pixels[i][j] = schema.convert(color);
            }
        }

        //проходим по массиву и записываем символы в строковую переменную с помощью StringBuilder
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                String str = Character.toString(pixels[i][j]) + Character.toString(pixels[i][j]);
                stringBuilder.append(str);

            }
            stringBuilder.append("\n");
            if (i == 0) {
                System.out.println(stringBuilder);
            }
        }

        return String.valueOf(stringBuilder);
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;

    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;

    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {

    }
}
