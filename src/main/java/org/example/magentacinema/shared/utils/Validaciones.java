package org.example.magentacinema.shared.utils;

public class Validaciones {
    /**
     * Valida que el texto no sea nulo, no esté vacío y no contenga solo espacios en blanco
     * @param texto el texto a validar
     * @throws IllegalArgumentException si el texto es inválido
     */
    public static void validarTexto(String texto) throws IllegalArgumentException {
        if (texto == null) {
            throw new IllegalArgumentException("El texto no puede ser nulo");
        }
        if (texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto no puede estar vacío o contener solo espacios");
        }
    }

    /**
     * Valida que el número esté dentro del rango especificado (inclusive)
     * @param entregado el número a validar
     * @param min el valor mínimo permitido (inclusive)
     * @param max el valor máximo permitido (inclusive)
     * @throws IllegalArgumentException si el número está fuera del rango
     */
    public static void validarNumero(int entregado, int min, int max) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("El valor mínimo no puede ser mayor que el máximo");
        }
        if (entregado < min) {
            throw new IllegalArgumentException(
                    String.format("El valor %d es menor que el mínimo permitido (%d)", entregado, min)
            );
        }
        if (entregado > max) {
            throw new IllegalArgumentException(
                    String.format("El valor %d es mayor que el máximo permitido (%d)", entregado, max)
            );
        }
    }

    // Métodos adicionales que podrían ser útiles para validaciones de dominio

    /**
     * Valida que el texto tenga una longitud específica
     * @param texto el texto a validar
     * @param longitudMinima longitud mínima permitida
     * @param longitudMaxima longitud máxima permitida
     */
    public static void validarLongitudTexto(
            String texto,
            int longitudMinima,
            int longitudMaxima
    ) throws IllegalArgumentException {
        validarTexto(texto); // Primero valida que no sea nulo o vacío
        int longitud = texto.trim().length();
        if (longitud < longitudMinima) {
            throw new IllegalArgumentException(
                    String.format("El texto debe tener al menos %d caracteres", longitudMinima)
            );
        }
        if (longitud > longitudMaxima) {
            throw new IllegalArgumentException(
                    String.format("El texto no puede tener más de %d caracteres", longitudMaxima)
            );
        }
    }

    /**
     * Valida que el número sea positivo
     * @param numero el número a validar
     */
    public static void validarPositivo(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número debe ser positivo");
        }
    }

    /**
     * Valida que el número no sea negativo
     * @param numero el número a validar
     */
    public static void validarNoNegativo(int numero) {
        if (numero < 0) {
            throw new IllegalArgumentException("El número no puede ser negativo");
        }
    }
}
