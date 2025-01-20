var modelList;

async function loadFeatures() {


    const response = await  fetch("LoadFeatures");


    if (response.ok) {
        const json = await response.json();

        const popup = Notification();

        const categoryList = json.categoryList;
        const brandList = json.brandList
        modelList = json.modelList;
        const originList = json.originList;
        const colourList = json.colourList;
        const conditionList = json.conditionList;

        loadSelect("categorySelect", categoryList, "name");
        loadSelect("brandSelect", brandList, "name");
        //loadSelect("modelSelect", modelList, "name");
        loadSelect("originSelect", originList, "name");
        loadSelect("colourSelect", colourList, "name");
        loadSelect("conditionSelect", conditionList, "name");

    } else {
        popup.error({
            message: "Please try again later!"
        });
    }

}

function loadSelect(selectTagId, list, property) {

    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item.id;
        optionTag.innerHTML = item[property];
        selectTag.appendChild(optionTag);
    });

}

function updateModels() {

    let selectedBrandId = document.getElementById("brandSelect").value;

    let modelSelectTag = document.getElementById("modelSelect");
    modelSelectTag.length = 1;

    modelList.forEach(model => {
        if (model.brand.id == selectedBrandId) {
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            modelSelectTag.appendChild(optionTag);
        }
    });
}

async function productListing() {

    const popup = Notification();

    const categorySelectTag = document.getElementById("categorySelect");
    const brandSelectTag = document.getElementById("brandSelect");
    const modelSelectTag = document.getElementById("modelSelect");
    const titleTag = document.getElementById("title");
    const descriptionTag = document.getElementById("description");
    const warrantyTag = document.getElementById("warranty");
    const originSelectTag = document.getElementById("originSelect");
    const colourSelectTag = document.getElementById("colourSelect");
    const conditionSelectTag = document.getElementById("conditionSelect");
    const priceTag = document.getElementById("price");
    const quantityTag = document.getElementById("quantity");
    const image1Tag = document.getElementById("image1");
    const image2Tag = document.getElementById("image2");
    const image3Tag = document.getElementById("image3");

    const data = new FormData();
    data.append("categoryId", categorySelectTag.value);
    data.append("brandId", brandSelectTag.value);
    data.append("modelId", modelSelectTag.value);
    data.append("title", titleTag.value);
    data.append("description", descriptionTag.value);
    data.append("warranty", warrantyTag.value);
    data.append("originId", originSelectTag.value);
    data.append("colourId", colourSelectTag.value);
    data.append("conditionId", conditionSelectTag.value);
    data.append("price", priceTag.value);
    data.append("quantity", quantityTag.value);
    data.append("image1", image1Tag.files[0]);
    data.append("image2", image2Tag.files[0]);
    data.append("image3", image3Tag.files[0]);

    const response = await  fetch("ProductListing",
            {
                method: "POST",
                body: data
            }
    );


    if (response.ok) {
        const json = await response.json();

        if (json.success) {

            reset();

            popup.success({
                message: json.content
            });
            setInterval(3000);
            window.location.reload();

            reset();


        } else {
            popup.error({
                message: json.content
            });
            
            reset();
        }

    } else {

        popup.error({
            message: "Please try again later!"
        });
        
        reset();

    }

    function reset() {
        categorySelectTag.value = 0;
        brandSelectTag.length = 0;
        modelSelectTag.length = 1;
        titleTag.value = "";
        descriptionTag.value = "";
        warrantyTag.value = "";
        originSelectTag.value = 0;
        colourSelectTag.value = 0;
        conditionSelectTag.value = 0;
        priceTag.value = "";
        quantityTag.value = 1;
        image1Tag.value = null;
        image2Tag.value = null;
        image3Tag.value = null;
    }

}

