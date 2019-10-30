function displaymessage() {
    JSInterface.showToast();
}

function showSpoilerInPost(spoiler) {
    var spoilerBody = spoiler.querySelector('.spoiler_text');

    if (spoilerBody.style.display === "none") {
        spoilerBody.style.display = "block"
    } else {
        spoilerBody.style.display = "none"
    }
}

function showSpoilerInWindow(spoiler) {
    var parent = spoiler.parentNode;
    var spoilerBody = parent.querySelector('.spoiler_text');
    console.log();
    JSInterface.showSpoilerInWindow(spoilerBody.innerHTML);
}
