export interface SearchParams {
  city: string;
  priceMin: string;
  priceMax: string;
  propertyType: string;
}

export interface PropertyType {
  value: string;
  label: string;
}

export interface Property {
  id: number;
  title: string;
  price: number;
  city: string;
  quarter?: string;
  type: string;
  imageUrl?: string;
  description?: string;
}
