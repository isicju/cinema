const createCircleWithNumber = (parentElement, number) => {
    const circle = document.createElement('div');
    circle.classList.add('circle');
    circle.id = number;
    circle.textContent = number
    parentElement.appendChild(circle);

    circle.addEventListener('click', () => {
        checkBooking();
        if (circle.className !== "booked") {
            const firstName = prompt(`Резервирование места ${number}\nВведите ваше имя:`);
            const lastName = prompt(`Резервирование места ${number}\nВведите вашу фамилию:`);
        } else {
            alert("the seat is already booked\n")
        }
    });
}

const pageRendering = () => {
    const gridContainer = document.createElement('div');
    gridContainer.classList.add('grid-container');
    document.body.appendChild(gridContainer);

    let circleCounter = 1;

    for (let i = 0; i < 5; i++) {
        const row = document.createElement('div');
        row.classList.add('row');
        gridContainer.appendChild(row);

        for (let j = 0; j < 10; j++) {
            createCircleWithNumber(row, circleCounter);
            circleCounter++;
        }
    }
}

const checkBooking = () => {
    const CURRENT_URL = '';
    fetch('http://' + CURRENT_URL)
        .then(response => response.json())
        .then(listOfReservedSeats => {
            listOfReservedSeats.forEach(seat => {
                changeClassById(seat);
            })
        })
        .catch(error => console.log(error));
}

const changeClassById = (elementId) => {
    const element = document.getElementById(elementId);
    if (element) {
        element.className = "booked";
    }
};

window.addEventListener('load', pageRendering);

//фронт делался под пиво, пожалуйста не бейте