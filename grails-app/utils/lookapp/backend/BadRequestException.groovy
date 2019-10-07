package lookapp.backend

class BadRequestException extends RuntimeException{
    def BadRequestException(String message){
        super(message)
    }
}
