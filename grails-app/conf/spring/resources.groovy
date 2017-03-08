import grails.rest.render.errors.VndErrorJsonRenderer
import grails.rest.render.errors.VndErrorXmlRenderer
import org.softwood.security.CoffeeShopAppSecurityEventListener

// Place your Spring DSL code here
beans = {
    //security logging by declaring an event listener
    //alternative is registering callback closures in application.groovy
    coffeShopAppSecurityEventListener (CoffeeShopAppSecurityEventListener)

    //establish VND renderers and then set the mime types in application.yml, and
    // set into the Rest Accept header "Accept: application/vnd.error+json,application/json"
    vndJsonErrorRenderer (VndErrorJsonRenderer)
    vndXmlErrorRenderer (VndErrorXmlRenderer)
}
