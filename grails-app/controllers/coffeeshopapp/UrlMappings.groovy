package coffeeshopapp

class UrlMappings {

    static mappings = {
        //add alternate url mapping for rest based resources
        //put into namepsace 'api' so that views can ebe separated in /views/api folder
        "/api/post" (resources : "postRest", namespace:"api")
        "/api/guest/post" (resources : "postRest", namespace:"api")

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
