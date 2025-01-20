async function loadData() {
  const response = await fetch("LoadData");

  if (response.ok) {
    const json = await response.json();

    if (json.success) {
      // Load filter options (category, brand, condition, color, size)
      loadOptions("category", json.categoryList, "name");
      loadOptions("brand", json.brandList, "name");
      loadOptions("condition", json.conditionList, "name");
      loadOptions("colour", json.colourList, "name");

      updateProductView(json.productList);
    } else {
      Notification().error({ message: json.message });
    }
  } else {
    Notification().error({ message: "Unable to process your request. Please try again later!" });
  }
}

async function loadOptions(prefix, dataList, property) {
  const options = document.getElementById(prefix + "-options");
  options.innerHTML = "";

  dataList.forEach(data => {
    const li = document.createElement('li');
    const input = document.createElement('input');
    const a = document.createElement('a');

    input.type = "radio";
    input.name = prefix + "Radio";
    input.id = prefix + "-radio-" + data.id;
    input.value = data[property];

    a.id = prefix + "-a";
    a.innerText = data[property];

    li.appendChild(input);
    li.appendChild(a);
    options.appendChild(li);
  });

  // Ensure only one radio button is selected
  const allInputs = options.querySelectorAll('input[type="radio"]');
  allInputs.forEach(input => {
    input.addEventListener('click', function () {
      allInputs.forEach(i => i.parentElement.classList.remove('chosen'));
      this.parentElement.classList.add('chosen');
    });
  });
}

async function searchProducts(firstResult) {
  const category = document.querySelector('#category-options .chosen input')?.value || '';
  const brand = document.querySelector('#brand-options .chosen input')?.value || '';
  const condition = document.querySelector('#condition-options .chosen input')?.value || '';
  const colour = document.querySelector('#colour-options .chosen input')?.value || '';
  const priceRangeStart = $('#slider-range').slider('values', 0);
  const priceRangeEnd = $('#slider-range').slider('values', 1);

  const data = {
    firstResult,
    category_name: category,
    brand_name: brand,
    condition_name: condition,
    colour_name: colour,
    price_range_start: priceRangeStart,
    price_range_end: priceRangeEnd
  };

  const response = await fetch("SearchProducts", {
    method: "POST",
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json"
    }
  });

  if (response.ok) {
    const json = await response.json();
    if (json.success) {
      updateProductView(json.productList);
    } else {
      Notification().error({ message: "No products found!" });
    }
  } else {
    Notification().error({ message: "Error in processing search." });
  }
}

async function updateProductView(products) {
  const productContainer = document.getElementById('product-container');
  productContainer.innerHTML = ''; // Clear old products

  products.forEach(product => {
    const productTemplate = `
      <div class="col">
        <div class="product-card mt-5" id="similar-product-${product.id}" style="height: 24rem;">
          <img id="similar-image" src="product-images/${product.id}/image1.png" class="mini-image-size rounded mt-3 d-block mx-auto" alt="${product.title}">
          <h5 class="mt-3 text-center mx-2">${product.title}</h5>
          <p class="text-warning text-center mb-3">Rs. ${product.price}.00</p>
          <div class="hover-icons mt-5 d-flex justify-content-center">
            <button class="panel-btn"><i class="bi bi-eye"></i></button>
            <button class="panel-btn" style="width: 40%; border-radius: 5%;" onclick="window.location.href='singleproduct.html?id=${product.id}';"><i class="bi bi-tag"></i>&nbsp;&nbsp;Shop Now</button>
            <button class="panel-btn"><i class="bi bi-cart"></i></button>
          </div>
        </div>
      </div>
    `;

    productContainer.innerHTML += productTemplate;
  });

  // Update pagination based on total product count
  const totalProductPages = Math.ceil(products.length / 6);
  updatePagination(totalProductPages);
}

currentProductPage = 1;

function updatePagination(totalProductPages) {
  const paginationContainer = document.querySelector('.pagination');
  paginationContainer.innerHTML = '';

  // Check if there are any products to display pages for
  if (totalProductPages > 0) {
    if (currentProductPage > 0) {
      const prevButton = createPaginationButton('chevron-left', currentProductPage - 1);
      paginationContainer.appendChild(prevButton);
    }

    // Create page buttons for each page number
    for (let i = 0; i < totalProductPages; i++) {
      const pageButton = createPaginationButton(i + 1, i);
      paginationContainer.appendChild(pageButton);
    }

    // Create "Next" button if not on the last page
    if (currentProductPage < totalProductPages - 1) {
      const nextButton = createPaginationButton('chevron-right', currentProductPage + 1);
      paginationContainer.appendChild(nextButton);
    }

    // Update styling of the active page button
    const activePageButton = paginationContainer.querySelector('.page-item.active');
    if (activePageButton) {
      activePageButton.classList.remove('active');
    }
    paginationContainer.querySelector(`.page-item:nth-child(${currentProductPage + 1})`).classList.add('active');
  }
}

function createPaginationButton(label, page) {
  const li = document.createElement('li');
  li.classList.add('page-item');

  const a = document.createElement('a');
  a.classList.add('page-link');

  if (label === 'chevron-left' || label === 'chevron-right') {
    const icon = document.createElement('i');
    icon.classList.add('bi', label); // Use Bootstrap icon class
    a.appendChild(icon);
  } else {
    a.innerText = label;
  }

  a.href = "#";
  a.addEventListener('click', (e) => {
    e.preventDefault();
    currentPage = page;
    searchProducts(currentPage * 6);
  });

  li.appendChild(a);
  return li;
}