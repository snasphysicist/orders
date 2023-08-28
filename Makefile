
down:
	docker-compose down
	docker-compose rm -f -v

up:
	$(MAKE) down
	docker-compose up -d

test:
	$(MAKE) up
	lein test
	$(MAKE) down

.PHONY: down up test
