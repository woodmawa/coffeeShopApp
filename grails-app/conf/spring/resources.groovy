import org.softwood.security.CoffeeShopAppSecurityEventListener

// Place your Spring DSL code here
beans = {
    //security logging by declaring an event listener
    //alternative is registering callback closures in application.groovy
    coffeShopAppSecurityEventListener (CoffeeShopAppSecurityEventListener)
}
