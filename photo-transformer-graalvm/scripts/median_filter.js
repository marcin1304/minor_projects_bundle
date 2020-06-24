// median filter, border pixels are not copied, simple implementation (red value has usually highest impact and blue lowest)
function fun() {
    for(let x = 1; x < originalImage.getWidth() - 1; x++)
        for(let y = 1; y < originalImage.getHeight() - 1; y++) {
            window = [];
            window.push(originalImage.getRGB(x - 1, y - 1));
            window.push(originalImage.getRGB(x, y - 1));
            window.push(originalImage.getRGB(x + 1, y - 1));

            window.push(originalImage.getRGB(x - 1, y));
            window.push(originalImage.getRGB(x, y));
            window.push(originalImage.getRGB(x + 1, y));

            window.push(originalImage.getRGB(x - 1, y + 1));
            window.push(originalImage.getRGB(x, y + 1));
            window.push(originalImage.getRGB(x + 1, y + 1));

            window.sort();

            transformedImage.setRGB(x, y, window[4]);
        }
}
