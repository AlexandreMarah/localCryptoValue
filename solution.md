# Localized cryptocurrency value

The purpose of this JAVA web application is to fecth the current localized price of a cryptocurrency.

### Requirements

- Java JDK 11

### Build the service

    ./gradlew build

### Boot the service

    ./gradlew bootRun

### Behavior

> You can access the main page of the web application at `http://localhost:8080/getCryptoList` (when running locally).
> 
> There are two pages : 
> - `/getCryptoList` retrieves a certain number of cryptocurrencies (defined as `COIN_GECKO_CRYPTO_LIST_SIZE` variable) 
> based on the Coin Gecko API. The retrieved cryptocurrencies are the ones with the biggest coin market capitalization. 
> <br/> You can select a cryptocurrency by its name in a select field and enter an optional IP address to get the current localized currency value.
> <br/> The IP address is used to get the localized locale and thus currency to display currency value accordingly.
> 
> - `/getLocalCryptoValue` retrieves the localized currency value based on the IP address provided at the previous page or will 
> use the IP address of the client's request otherwise.
> <br/> An other link is present to go back to the cryptocurrencies list.

### Remarks

- Both pages have caches : 
  - the list of cryptocurrencies is retrieved at maximum once every 10 minutes
  - the value of a particular cryptocurrency for a particular IP is retrieved at maximum once every 10 seconds

- The description of Coin Gecko APIs is available at this address : https://www.coingecko.com/en/api/documentation

- In case of using the service locally, the IP address will be necessary to have a valid localization.
