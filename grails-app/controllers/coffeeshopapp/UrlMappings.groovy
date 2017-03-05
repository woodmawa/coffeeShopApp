package coffeeshopapp

class UrlMappings {

    static mappings = {
        //add alternate url mapping for rest based resources
        "/api/posts" (resources : "postRest")

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
