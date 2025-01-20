async function verifyAccount(){
    const dto = {
        verification: document.getElementById("verification").value
    };

    const response = await  fetch("Verification",
            {
                method: "POST",
                body: JSON.stringify(dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );


    if (response.ok) {
        const json = await response.json();

        const popup = Notification();

        if (json.success) {
            window.location = "signin.html";

        } else {
            popup.error({
                message: json.content
            });

        }

    } else {
        popup.error({
            message: "Please try again later!"
        });
    }
}