// Payment completed. It can be a success or failure.
payhere.onCompleted = function onCompleted(orderId) {

    const popup = Notification();

    popup.success({
        message: "Order Placed. Thank You"
    });

    window.location = "index.html";
};

// Payment window closed
payhere.onDismissed = function onDismissed() {

    const popup = Notification();

    popup.error({
        message: "Order Dismissed"
    });
};

// Error occurred
payhere.onError = function onError(error) {

    const popup = Notification();

    popup.error({
        message: "An Error Occurred"
    });
};



async function loadData() {

    const response = await  fetch("LoadCheckout");
    const popup = Notification();

    if (response.ok) {

        const json = await response.json();

        if (json.success) {

            //Store Response Data
            const address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;

            //load cities
            let citySelect = document.getElementById("city");
            citySelect.length = 1;
            cityList.forEach(city => {
                let cityOption = document.createElement("option");
                cityOption.value = city.id;
                cityOption.innerHTML = city.name;
                citySelect.appendChild(cityOption);
            });


            //load current address
            let currentAddressCheckbox = document.getElementById("checkbox1");
            currentAddressCheckbox.addEventListener("change", e => {

                let first_name = document.getElementById("first-name");
                let last_name = document.getElementById("last-name");
                let city = document.getElementById("city");
                let address1 = document.getElementById("line1");
                let address2 = document.getElementById("line2");
                let postal_code = document.getElementById("postal-code");
                let mobile = document.getElementById("mobile");

                if (currentAddressCheckbox.checked) {
                    first_name.value = address.first_name;
                    last_name.value = address.last_name;

                    city.value = address.city.id;
                    city.disabled = true;
                    city.dispatchEvent(new Event("change"));


                    address1.value = address.line1;
                    address2.value = address.line2;
                    postal_code.value = address.postal_code;
                    mobile.value = address.mobile;
                } else {
                    first_name.value = "";
                    last_name.value = "";

                    city.value = 0;
                    city.disabled = false;
                    city.dispatchEvent(new Event("change"));

                    address1.value = "";
                    address2.value = "";
                    postal_code.value = "";
                    mobile.value = "";
                }
            });
            //load cart items
            let co_tbody = document.getElementById("co-tbody");
            let co_item_tr = document.getElementById("co-item-tr");
            let co_order_subtotal_tr = document.getElementById("co-order-subtotal-tr");
            let co_order_shipping_tr = document.getElementById("co-order-shipping-tr");
            let co_order_total_tr = document.getElementById("co-order-total-tr");
            co_tbody.innerHTML = "";

            let sub_total = 0;

            cartList.forEach(item => {

                let co_item_clone = co_item_tr.cloneNode(true);
                co_item_clone.querySelector("#co-item-title").innerHTML = item.product.title;
                co_item_clone.querySelector("#co-item-qty").innerHTML = item.qty;
                let item_sub_total = item.product.price * item.qty;
                sub_total += item_sub_total;

                co_item_clone.querySelector("#co-item-subtotal").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(item_sub_total);

                co_tbody.appendChild(co_item_clone);
            });


            co_order_subtotal_tr.querySelector("#co-subtotal").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(sub_total);

            co_tbody.appendChild(co_order_subtotal_tr);

            citySelect.addEventListener("change", e => {
                //update shipping charges

                //get cart item count
                let item_count = cartList.length;

                let shipping_amount = 0;

                //check city = Colombo or not
                if (citySelect.value == 1) {
                    //Colombo
                    shipping_amount = item_count * 800;
                } else {
                    shipping_amount = item_count * 1200;
                }

                co_order_shipping_tr.querySelector("#co-shipping-amount").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(shipping_amount);
                co_tbody.appendChild(co_order_shipping_tr);

                //update total
                let total = sub_total + shipping_amount;

                co_order_total_tr.querySelector("#co-total").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(total);
                co_tbody.appendChild(co_order_total_tr);
            });

            city.dispatchEvent(new Event("change"));

        } else {
            window.location = "signin.html";
        }

    } else {
        popup.error({
            message: "Unable to process your request!"
        });
    }

}

async function checkout() {
    const popup = Notification();

    let isCurrentAddress = document.getElementById("checkbox1").checked;

    let first_name = document.getElementById("first-name");
    let last_name = document.getElementById("last-name");
    let city = document.getElementById("city");
    let address1 = document.getElementById("line1");
    let address2 = document.getElementById("line2");
    let postal_code = document.getElementById("postal-code");
    let mobile = document.getElementById("mobile");

    const data = {
        isCurrentAddress: isCurrentAddress,
        first_name: first_name.value,
        last_name: last_name.value,
        city_id: city.value,
        address1: address1.value,
        address2: address2.value,
        postal_code: postal_code.value,
        mobile: mobile.value
    };

    const response = await fetch("Checkout", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            payhere.startPayment(json.payhereJson);
        } else {
            popup.error({
                message: json.message
            });
        }
    } else {
        popup.error({
            message: "Unable to process your request. Please try again later!"
        });
    }
}

