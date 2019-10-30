package lookapp.backend

import grails.rest.Resource

@Resource(uri='/points')
class Point {

    //valor utilizado para sumar puntos
    int changePay
    //valor utilizado para pagar
    int changePurchase

    static constraints = {
    }
}

