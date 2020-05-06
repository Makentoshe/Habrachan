function displaymessage() {
    JSInterface.showToast();
}

function onSpoilerClickedListener(spoilerTitle) {
    var spoiler = spoilerTitle.parentNode;
    var spoilerBody = spoiler.querySelector('.spoiler_text');

    if (spoilerBody.style.display === "none") {
        spoilerBody.style.display = "block"
    } else {
        spoilerBody.style.display = "none"
    }
}

function onImageClickedListener(image) {
    var imageSource = image.src;
    JSInterface.onImageClickedListener(imageSource);
}