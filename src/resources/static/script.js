document.addEventListener("DOMContentLoaded", function() {
    console.log("Transportation Management Application Loaded");

    let links = document.querySelectorAll("nav ul li a");
    links.forEach(link => {
        link.addEventListener("mouseover", function() {
            this.style.color = "#ffcc00";
        });
        link.addEventListener("mouseout", function() {
            this.style.color = "white";
        });
    });
});
