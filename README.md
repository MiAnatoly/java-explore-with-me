# java-explore-with-me

# https://github.com/MiAnatoly/java-explore-with-me/pull/3

# Комментарии
### Комментарии выступают в роли комуникации для общения и получения дополнительной информации от пользователей о опубликованных событиях. Беседы общие, без возможности частного общения.

###    закрытый
    - добавить комментарий
    комментарий может добавить любой пользователь, в том числе и регистратор события
    ограничение на количество комментариев отсутствует.
    - обновить комментарий может только регистратор комментария.
    - просмотреть свой комментарий по id
    - просмотреть все свои комментарии
    - удалить свой комментарий

###    открытый
    - просмотреть комментарии к событию 
    сортируется по дате и отображается постранично 

###    административный
    - удалить комментарий

#### schema

    NewCommentDto

    description        String               Size(min = 5, max = 2000)
    eventId            Long             
#
    CommentUserDto 

    id                 Long            
    description        String               Size(min = 5, max = 2000)
    created            LocalDateTime
    eventId            Long 
#
    CommentDto {
    id                  Long
    description         String              Size(min = 5, max = 2000)
    created             LocalDateTime
    eventId             Long
    requester           UserShortDto

    
    