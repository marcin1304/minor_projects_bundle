function fun() {
    for(let x = 0; x < originalImage.getWidth(); x++)
        for(let y = 0; y < originalImage.getHeight(); y++)
            transformedImage.setRGB(x, y, 0xffffff - originalImage.getRGB(x, y));
}