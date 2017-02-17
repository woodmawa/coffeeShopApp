/**
 * Created by will on 14/02/2017.
 */

def postCode = ["IP4 2TH",
                "ip4 2th",
                "A9 9ZZ",
                "GIR 0AA",
                "A9C 9ZZ",
                "AD9E 9ZZ"
    ]

//bloody hell !!
String regex = /^([gG][iI][rR] {0,}0[aA]{2})|([a-pr-uwyzA-PR-UWYZ](([0-9](([0-9]|[a-hjkstuwA-HJKSTUW])?)?)|([a-hk-yA-HK-Y][0-9]([0-9]|[abehmnprvwxyABEHMNPRVWXY])?)) ?[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2})$/
        //didnt work /^(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])|([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y]))) {0,}[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2}))\$/

postCode.each {
    if (it.matches (regex))
        println "$it passes regex test"
    else
        println "$it failed regex test "
}