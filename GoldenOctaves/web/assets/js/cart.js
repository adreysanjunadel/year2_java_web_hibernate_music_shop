async function loadCartItems() {

    const response = await fetch("LoadCartItems");

    const popup = Notification();

    if (response.ok) {
        const json = await response.json();

        if (json.length == 0) {

            popup.error({
                message: "Your Cart is Empty"
            });

            //window.location = "index.html";

        } else {

            let cartItemContainer = document.getElementById("cart-item-container");
            let cartItem = document.getElementById("cart-item");

            cartItemContainer.innerHTML = "";

            let totalQty = 0;
            let total = 0;

            json.forEach(item => {

                let itemSubTotal = item.product.price * item.qty;

                totalQty += item.qty;
                total += itemSubTotal;

                let cartItemClone = cartItem.cloneNode(true);  // deep clone

                cartItemClone.querySelector("#cart-item-a").href = "singleproduct.html?id=" + item.product.id;
                cartItemClone.querySelector("#cart-item-image").src = "product-images/" + item.product.id + "/image1.png";
                cartItemClone.querySelector("#cart-item-title").innerHTML = item.product.title;
                cartItemClone.querySelector("#cart-item-price").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
                ).format(item.product.price);
                cartItemClone.querySelector("#cart-item-qty").value = item.qty;
                cartItemClone.querySelector("#cart-item-subtotal").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
                ).format(itemSubTotal);

                document.getElementById("cart-item-container").appendChild(cartItemClone);

            });

            document.getElementById("cart-total-qty").innerHTML = totalQty;
            document.getElementById("cart-total").innerHTML = new Intl.NumberFormat(
                "en-US",
                {
                    minimumFractionDigits: 2
                }
            ).format(total);
        }
        
    } else {
        popup.error({
            message: "Unable to process your request!"
        });
    }

}

// Remove Item Script
document.getElementById("cart-item-container").addEventListener("click", async function(e) {
    if (e.target && e.target.closest(".remove-btn")) {  // Updated to use class
        const row = e.target.closest("tr");  // Get the row of the clicked button
        const productId = row.querySelector("#cart-item-a").href.split('id=')[1];  // Get product ID from href

        const response = await fetch("CartItemRemoval", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                action: "removeItem",
                productId: productId
            })
        });

        if (response.ok) {
            row.remove();  // Remove the row from the table
            updateCartSummary();  // Update total price and quantity
        } else {
            alert("Error removing item from cart.");
        }
    }
});

// Clear Cart Script
document.getElementById("clear-btn").addEventListener("click", async function() {
    const response = await fetch("CartItemRemoval", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            action: "clearCart"
        })
    });

    if (response.ok) {
        document.getElementById("cart-item-container").innerHTML = "";  // Clear all cart items
        updateCartSummary();  // Update total price and quantity
    } else {
        alert("Error clearing cart.");
    }
});

// Function to update the order summary after changes
function updateCartSummary() {
    let totalQty = 0;
    let total = 0;

    // Loop through each item and calculate totals
    document.querySelectorAll("#cart-item-container tr").forEach(row => {
        const qty = parseInt(row.querySelector("#cart-item-qty").value);
        const price = parseFloat(row.querySelector("#cart-item-price").textContent.replace(/,/g, ''));  // Remove commas

        totalQty += qty;
        total += qty * price;
    });

    // Update order summary values
    document.getElementById("cart-total-qty").textContent = totalQty;
    document.getElementById("cart-total").textContent = new Intl.NumberFormat(
        "en-US", { minimumFractionDigits: 2 }
    ).format(total);
}




