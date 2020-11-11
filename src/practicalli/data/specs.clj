(ns practicalli.data.specs
  (:require

   ; here for debugging in repl
   [crux.api :as crux]

   ; eql for projection syntax:


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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;          OUR CODE           ;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
;; 



(spec/def :charge/type #{:CHARGE})
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
;; (spec/def :line-item/charge :line-item/common)
(spec/def :line-item/charge (spec/merge :line-item/common
                                        (spec/keys :req [:charge/type])))

(spec/def :line-item/payment :line-item/common)
(spec/def :line-item/interest :line-item/common)
(spec/def :line-item/fee :line-item/common)



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment



  (spec-gen/generate (spec/gen :line-item/charge))

  (do
    (require '[edn-query-language.core :as eql])
    (def crux
      (crux/start-node {}))

    (def charge1
      {:crux.db/id "24e56a67-0559-4baa-a16e-9fab034cc84c"
       :account-id "8c899527-fe21-490a-ae45-a58488540806"
       :product-id "f0b06263-73c3-44a8-a261-ec0e419ef089"
       :interest-rate 4.5
       :amount-cents 1800})

    (crux/submit-tx crux [[:crux.tx/put charge1]])

    (crux/q (crux/db crux)
            '{:find [(eql/project ?line-item/charge [*])]
              :where [[?line-item/charge :crux.db/id "24e56a67-0559-4baa-a16e-9fab034cc84c"]]})


    (crux/entity-history (crux/db crux) "24e56a67-0559-4baa-a16e-9fab034cc84c" :asc))












  (spec-gen/generate (spec/gen :customer/id))

  (spec/valid? :customer/id
               (spec-gen/generate (spec/gen :customer/id)))

  (spec/valid? :customer/id
               #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"})
  ;; => false

  (spec/valid? :customer/id
               (:id #:customer{:id #uuid "323ecc53-d676-4b7a-bea0-a4b3ca075c2c"}))) ;; End of rich comment block
