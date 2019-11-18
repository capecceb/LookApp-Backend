package lookapp.backend

import grails.rest.RestfulController

class PromotionController extends RestfulController {

    PromotionController() {
        super(Promotion)
    }

    static responseFormats = ['json', 'xml']

    def search() {
        def res = [:]
        def params = getParams()
        if(params.date==null){
            res["message"] = "the date param is required"
            respond(res, status: 400)
            return
        }
        Date date = DateTimeParser.parseSearchFormat(params.date)

        def promotionCriteria = Promotion.createCriteria()
        List<Promotion> promotions = promotionCriteria.list {
            lte("startDate", date)
            gte("endDate", date)
            eq("status", PromotionStatus.ACTIVE)
        }
        respond(promotions, status: 200)
    }
}
