(ns practicalli.data.specs
  (:require
   [clojure.spec.alpha     :as spec]
   [clojure.spec.gen.alpha :as spec-gen]))



;; Customer detail specifications
(spec/def :customer/id uuid?)
(spec/def :customer/legal-name string?)
(spec/def :customer/email-address string?)
(spec/def :customer/residential-address string?)
(spec/def :customer/social-security-number string?)
(spec/def :customer/preferred-name string?)

;; Data to send to the database
(spec/def :customer/unregistered
  (spec/keys
   :req [:customer/legal-name
         :customer/email-address
         :customer/residential-address
         :customer/social-security-number]
   :opt [:customer/preferred-name]))


;; Data received from the database
(spec/def :customer/registered
  (spec/keys
   :req [:customer/id
         :customer/legal-name
         :customer/email-address
         :customer/residential-address
         :customer/social-security-number]
   :opt [:customer/preferred-name]))


;; Define attributes that are common to a line item
(spec/def :line-item/id uuid?)
(spec/def :line-item/account-id uuid?)
(spec/def :line-item/product-id uuid?)
(spec/def :line-item/interest-rate double?)
(spec/def :line-item/amount-cents double?)
(spec/def :line-item/merchant-info string?)
(spec/def :line-item/description string?)

;; NOTE: 
;; I dont think we need these. When we query data, we will know
;; it's a charge because of the entity type, and payment and schedule orders
;; can be defined in the application layer.
;; 
;; (spec/def :charge/type "CHARGE")
;; (spec/def :charge/payment-order 3)
;; (spec/def :charge/schedule-order 3)

(spec/def :line-item/common
  (spec/keys
   :req [:line-item/id
         :line-item/account-id
         :line-item/product-id
         :line-item/interest-rate
         :line-item/amount-cents]
   :opt [:line-item/merchant-info :line-item/description]))

;; NOTE: when we need to customize, extend the line-item interface via merge:
;; https://clojure.org/guides/spec#_entity_maps                        
(spec/def :line-item/charge :line-item/common)
(spec/def :line-item/payment :line-item/common)
(spec/def :line-item/interest :line-item/common)
(spec/def :line-item/fee :line-item/common)




;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (spec-gen/generate (spec/gen :customer/unregistered))
  ;; => #:customer{:legal-name "7hl4PT89AO3Pe04958YBWxWH0m6tnG", :email-address "iz83P60EtVM9lMX6zg6", :residential-address "FJ7Mh6nNJviX", :social-security-number "9bYAS85axW42KnOPcPjMtkg06qb4Tr"}
  (spec-gen/generate (spec/gen :customer/registered))
  ;; => #:customer{:preferred-name "S8i45tGAgaO60uPVW6q48Emg1", :legal-name "FFsv7pCavtC5V9qD52wO91i9Y", :email-address "6Wl3O11i3L66q800f3JcgkQ7414V0", :residential-address "vzl93YDnD74Zh5", :social-security-number "120J"}


  (spec-gen/generate (spec/gen :customer/id))

  (spec/valid? :customer/id
               (spec-gen/generate (spec/gen :customer/id)))

  (spec/valid? :customer/id
               #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"})
  ;; => false

  (spec/valid? :customer/id
               (:id #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"}))) ;; End of rich comment block
