-- Script para crear las tablas en PostgreSQL
-- Ejecutar este script en la base de datos consumo_apis

-- Tabla para respuestas de Google Places
CREATE TABLE IF NOT EXISTS google_places_responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    query TEXT NOT NULL,
    status VARCHAR(50),
    places JSONB,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    raw_response TEXT
);

-- Tabla para respuestas de IP Geo Location
CREATE TABLE IF NOT EXISTS ip_geo_responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ip_address VARCHAR(45),
    country VARCHAR(100),
    region VARCHAR(100),
    city VARCHAR(100),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    timezone VARCHAR(50),
    isp VARCHAR(255),
    org VARCHAR(255),
    "as" VARCHAR(255),
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    raw_response TEXT
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_google_places_query ON google_places_responses(query);
CREATE INDEX IF NOT EXISTS idx_google_places_timestamp ON google_places_responses(timestamp);
CREATE INDEX IF NOT EXISTS idx_ip_geo_ip_address ON ip_geo_responses(ip_address);
CREATE INDEX IF NOT EXISTS idx_ip_geo_timestamp ON ip_geo_responses(timestamp);
