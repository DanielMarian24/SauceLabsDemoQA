# Jira Test Cases — SauceLabsMobileApp (Android + API)

App: Sauce Labs My Demo App Android (`mda-*.apk`). BE: `https://api.practicesoftwaretesting.com`.
Each case maps 1:1 to a TestNG method.

## UI (Appium Java+TestNG)

### M-TC-001 Catalog loads — `NavigationTest#catalogLoadsByDefault`
Pre: app installed. Steps: launch app. Expect: products screen visible, >0 items.

### M-TC-002 Side menu entries — `NavigationTest#sideMenuEntries`
Steps: launch app -> tap open menu. Expect: webview, qr code, geo location, drawing, about visible.

### M-TC-003 About — `NavigationTest#navigateToAbout`
Steps: launch app -> menu -> About. Expect: About screen.

### M-TC-004 Back to catalog — `NavigationTest#backToCatalog`
Steps: launch app -> menu -> Catalog. Expect: products screen.

### M-TC-005 Login happy — `LoginTest#loginHappyPath` (bob@example.com / 10203040)
Steps: launch app -> tap login -> enter valid credentials -> tap login. Expect: catalog + menu shows Log Out.

### M-TC-006 Locked user — `LoginTest#lockedUserError` (alice@example.com)
Steps: launch app -> tap login -> enter locked user credentials -> tap login. Expect: error contains "locked".

### M-TC-007 Invalid creds — `LoginTest#invalidCredentialsError`
Steps: launch app -> tap login -> enter invalid credentials -> tap login. Expect: generic error visible.

### M-TC-008 Empty fields — `LoginTest#emptyFieldsError`
Steps: launch app -> tap login -> leave fields empty -> tap login. Expect: error visible.

### M-TC-009 Catalog products — `CatalogTest#catalogShowsProducts`
Steps: launch app -> tap catalog. Expect: >=2 items, first name non-blank.

### M-TC-010 Sort NameAsc — `CatalogTest#sortByNameAsc`
Steps: launch app -> tap catalog -> sort -> NameAsc. Expect: list still loaded.

### M-TC-011 Sort PriceDesc — `CatalogTest#sortByPriceDesc`
Steps: launch app -> tap catalog -> sort -> PriceDesc. Expect: products visible after sort.

### M-TC-012 Detail — `CatalogTest#openProductDetail`
Steps: launch app -> tap catalog -> tap first item. Expect: detail name == list name, price non-blank.

### M-TC-013 Add to cart — `CartTest#addToCart`
Steps: launch app -> tap catalog -> tap first item -> tap add to cart. Expect: cart 1 item.

### M-TC-014 Qty total — `CartTest#changeQuantity`
Steps: launch app -> tap catalog -> add item to cart -> go to cart -> increase qty. Expect: total changes.

### M-TC-015 Remove — `CartTest#removeItem`
Steps: launch app -> tap catalog -> add item to cart -> go to cart -> remove item. Expect: cart empty.

### M-TC-016 Two products — `CartTest#addTwoProducts`
Steps: launch app -> tap catalog -> add index 0 + 1. Expect: 2 items.

### M-TC-017 Full checkout — `CheckoutTest#fullCheckout`
Steps: launch app -> tap catalog -> add item to cart -> go to cart -> checkout -> login (if asked) -> address John Doe/123 Main St/San Jose/95110/US -> payment 4111111111111111 12/30 123 -> Place Order. Expect: Checkout Complete.

### M-TC-018 Bad payment blocked — `CheckoutTest#invalidPaymentBlocked`
Steps: launch app -> tap catalog -> add item to cart -> go to cart -> checkout -> login (if asked) -> address John Doe/123 Main St/San Jose/95110/US -> payment 1/1/1 -> Place Order. Expect: NOT complete.

### M-TC-019 Add product with quantity — `CartTest#addToCartWithQuantity`
Steps: launch app -> tap catalog -> tap first item -> select quantity (2 or more) -> tap add to cart. Expect: cart shows correct quantity of items.

## API (RestAssured)

### M-API-001 Register 201 — `AuthApiTest#registerNewUser`
POST /users/register unique -> 201, id + email.

### M-API-002 Existing 409 — `AuthApiTest#registerExistingEmail`
### M-API-003 Weak 422 — `AuthApiTest#weakPassword`
### M-API-004 Login 200+token — `AuthApiTest#loginHappyPath`
### M-API-005 Login 401 — `AuthApiTest#loginInvalid`
### M-API-006 Empty 401 — `AuthApiTest#loginEmpty`
### M-API-007 Profile 200 — `AuthApiTest#getProfile` GET /users/me Bearer
### M-API-008 Bad token 401 — `AuthApiTest#profileInvalidToken`
### M-API-009 No token 401 — `AuthApiTest#profileMissingToken`
### M-API-010 List 200 — `ProductApiTest#productList` GET /products data/total/current_page
### M-API-011 Structure — `ProductApiTest#productStructure` price number, category+brand
### M-API-012 By ID 200 — `ProductApiTest#productById`
### M-API-013 404 — `ProductApiTest#product404`
### M-API-014 Search Plier — `ProductApiTest#searchPlier` ?name=Plier contains plier
### M-API-015 No-match 200 — `ProductApiTest#searchNoMatch`
### M-API-016 Multi-term — `ProductApiTest#searchMultiple` Hammer/Plier/Saw
### M-API-017 Cart lifecycle — `CartApiTest#cartLifecycle` POST /carts 201 -> add 200 -> view qty2 -> PUT qty5 -> DELETE 204 -> empty -> delete cart 204
### M-API-018 Order discovery — `CartApiTest#orderDiscovery` try /orders /order /carts/{id}/orders /checkout, document FE-only.

Total: 18 UI + 18 API = 36 automated.
