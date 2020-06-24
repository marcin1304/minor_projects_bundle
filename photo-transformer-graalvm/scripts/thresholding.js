// thresholding, turns colorful image into gray scale image, basing on average of rgb values
function fun() {
    for(let x = 0; x < originalImage.getWidth(); x++)
        for(let y = 0; y < originalImage.getHeight(); y++) {
            red =  (originalImage.getRGB(x, y) & 0xff0000) >>> 16;
            green =  (originalImage.getRGB(x, y) & 0xff00) >>> 8;
            blue =  originalImage.getRGB(x, y) & 0xff;
            grey_scale = Math.round(red / 3 + green / 3 + blue / 3)
            new_color = grey_scale + (grey_scale << 8) + (grey_scale << 16)
            transformedImage.setRGB(x, y, new_color);
        }
}