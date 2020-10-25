package com.mylocation.android.model;

import java.util.List;

public class LocationModel1  {


    /**
     * status : 1
     * regeocode : {"addressComponent":{"city":"襄阳市","province":"湖北省","adcode":"420606","district":"樊城区","towncode":"420606002000","streetNumber":{"number":"88号","location":"112.10093,32.0708519","direction":"西","distance":"735.722","street":"花卉大道"},"country":"中国","township":"王寨街道","businessAreas":[[]],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0710"},"formatted_address":"湖北省襄阳市樊城区王寨街道010乡道"}
     * info : OK
     * infocode : 10000
     */

    private String status;
    private RegeocodeBean regeocode;
    private String info;
    private String infocode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RegeocodeBean getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(RegeocodeBean regeocode) {
        this.regeocode = regeocode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public static class RegeocodeBean {
        /**
         * addressComponent : {"city":"襄阳市","province":"湖北省","adcode":"420606","district":"樊城区","towncode":"420606002000","streetNumber":{"number":"88号","location":"112.10093,32.0708519","direction":"西","distance":"735.722","street":"花卉大道"},"country":"中国","township":"王寨街道","businessAreas":[[]],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0710"}
         * formatted_address : 湖北省襄阳市樊城区王寨街道010乡道
         */

        private AddressComponentBean addressComponent;
        private String formatted_address;

        public AddressComponentBean getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentBean addressComponent) {
            this.addressComponent = addressComponent;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public static class AddressComponentBean {
            /**
             * city : 襄阳市
             * province : 湖北省
             * adcode : 420606
             * district : 樊城区
             * towncode : 420606002000
             * streetNumber : {"number":"88号","location":"112.10093,32.0708519","direction":"西","distance":"735.722","street":"花卉大道"}
             * country : 中国
             * township : 王寨街道
             * businessAreas : [[]]
             * building : {"name":[],"type":[]}
             * neighborhood : {"name":[],"type":[]}
             * citycode : 0710
             */

            private String city;
            private String province;
            private String adcode;
            private String district;
            private String towncode;
            private StreetNumberBean streetNumber;
            private String country;
            private String township;
            private BuildingBean building;
            private NeighborhoodBean neighborhood;
            private String citycode;
            private List<List<?>> businessAreas;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getTowncode() {
                return towncode;
            }

            public void setTowncode(String towncode) {
                this.towncode = towncode;
            }

            public StreetNumberBean getStreetNumber() {
                return streetNumber;
            }

            public void setStreetNumber(StreetNumberBean streetNumber) {
                this.streetNumber = streetNumber;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getTownship() {
                return township;
            }

            public void setTownship(String township) {
                this.township = township;
            }

            public BuildingBean getBuilding() {
                return building;
            }

            public void setBuilding(BuildingBean building) {
                this.building = building;
            }

            public NeighborhoodBean getNeighborhood() {
                return neighborhood;
            }

            public void setNeighborhood(NeighborhoodBean neighborhood) {
                this.neighborhood = neighborhood;
            }

            public String getCitycode() {
                return citycode;
            }

            public void setCitycode(String citycode) {
                this.citycode = citycode;
            }

            public List<List<?>> getBusinessAreas() {
                return businessAreas;
            }

            public void setBusinessAreas(List<List<?>> businessAreas) {
                this.businessAreas = businessAreas;
            }

            public static class StreetNumberBean {
                /**
                 * number : 88号
                 * location : 112.10093,32.0708519
                 * direction : 西
                 * distance : 735.722
                 * street : 花卉大道
                 */

                private String number;
                private String location;
                private String direction;
                private String distance;
                private String street;

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getDirection() {
                    return direction;
                }

                public void setDirection(String direction) {
                    this.direction = direction;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }

                public String getStreet() {
                    return street;
                }

                public void setStreet(String street) {
                    this.street = street;
                }
            }

            public static class BuildingBean {
                private List<?> name;
                private List<?> type;

                public List<?> getName() {
                    return name;
                }

                public void setName(List<?> name) {
                    this.name = name;
                }

                public List<?> getType() {
                    return type;
                }

                public void setType(List<?> type) {
                    this.type = type;
                }
            }

            public static class NeighborhoodBean {
                private List<?> name;
                private List<?> type;

                public List<?> getName() {
                    return name;
                }

                public void setName(List<?> name) {
                    this.name = name;
                }

                public List<?> getType() {
                    return type;
                }

                public void setType(List<?> type) {
                    this.type = type;
                }
            }
        }
    }
}
