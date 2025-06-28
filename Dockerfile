FROM php:8.2-apache
COPY app/ /var/www/html/
RUN docker-php-ext-install mysqli
