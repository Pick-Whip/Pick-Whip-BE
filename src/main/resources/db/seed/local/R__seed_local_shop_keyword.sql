INSERT INTO shop_appeal_keywords (keyword_type, keyword_text)
SELECT 'NORMAL', '생일'
    WHERE NOT EXISTS (SELECT 1 FROM shop_appeal_keywords WHERE keyword_text = '생일');

INSERT INTO shop_appeal_keywords (keyword_type, keyword_text)
SELECT 'NORMAL', '기념일'
    WHERE NOT EXISTS (SELECT 1 FROM shop_appeal_keywords WHERE keyword_text = '기념일');

INSERT INTO shop_appeal_keywords (keyword_type, keyword_text)
SELECT 'NORMAL', '파티'
    WHERE NOT EXISTS (SELECT 1 FROM shop_appeal_keywords WHERE keyword_text = '파티');

INSERT INTO shop_appeal_keywords (keyword_type, keyword_text)
SELECT 'NORMAL', '크리스마스'
    WHERE NOT EXISTS (SELECT 1 FROM shop_appeal_keywords WHERE keyword_text = '크리스마스');

SET @kw_birthday := (SELECT keyword_id FROM shop_appeal_keywords WHERE keyword_text='생일' LIMIT 1);
SET @kw_anniv    := (SELECT keyword_id FROM shop_appeal_keywords WHERE keyword_text='기념일' LIMIT 1);
SET @kw_party    := (SELECT keyword_id FROM shop_appeal_keywords WHERE keyword_text='파티' LIMIT 1);
SET @kw_xmas     := (SELECT keyword_id FROM shop_appeal_keywords WHERE keyword_text='크리스마스' LIMIT 1);

/*shop_keyword_mapping (없으면 추가) */
-- shop 1: 마포 스윗 케이크
INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 1, @kw_birthday
    WHERE @kw_birthday IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=1 AND keyword_id=@kw_birthday);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 1, @kw_anniv
    WHERE @kw_anniv IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=1 AND keyword_id=@kw_anniv);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 1, @kw_party
    WHERE @kw_party IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=1 AND keyword_id=@kw_party);

-- shop 2: 강남 스윗 케이크
INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 2, @kw_anniv
    WHERE @kw_anniv IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=2 AND keyword_id=@kw_anniv);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 2, @kw_party
    WHERE @kw_party IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=2 AND keyword_id=@kw_party);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 2, @kw_xmas
    WHERE @kw_xmas IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=2 AND keyword_id=@kw_xmas);

-- shop 11: 레망도레 광화문점
INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 11, @kw_xmas
    WHERE @kw_xmas IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=11 AND keyword_id=@kw_xmas);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 11, @kw_anniv
    WHERE @kw_anniv IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=11 AND keyword_id=@kw_anniv);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 11, @kw_party
    WHERE @kw_party IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=11 AND keyword_id=@kw_party);

-- shop 14: 더플라자호텔 블랑제리
INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 14, @kw_birthday
    WHERE @kw_birthday IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=14 AND keyword_id=@kw_birthday);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 14, @kw_anniv
    WHERE @kw_anniv IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=14 AND keyword_id=@kw_anniv);

INSERT INTO shop_keyword_mapping (shop_id, keyword_id)
SELECT 14, @kw_party
    WHERE @kw_party IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM shop_keyword_mapping WHERE shop_id=14 AND keyword_id=@kw_party);
