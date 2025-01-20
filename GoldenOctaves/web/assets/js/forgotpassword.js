async function forgotPassword() {
    
    const popup = Notification();

    const dto = {
        email: document.getElementById("email").value
    };

    const response = await  fetch("ForgotPassword",
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

        if (json.success) {
            window.location = "change-password.html";

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