-- review_keyword 테이블 seed 데이터
INSERT INTO review_keyword (
    category,
    code,
    label
) VALUES
      -- DESIGN_SATISFACTION
      ('DESIGN_SATISFACTION', 'MATCH_REQUEST', '원하는 디자인을 잘해줬어요'),
      ('DESIGN_SATISFACTION', 'PRETTY', '디자인이 예뻐요'),
      ('DESIGN_SATISFACTION', 'CREATIVE', '창의적이에요'),
      ('DESIGN_SATISFACTION', 'SPECIAL', '특별해요'),
      ('DESIGN_SATISFACTION', 'LUXURIOUS', '화려한'),

      -- SAME_AS_RESULT
      ('SAME_AS_RESULT', 'SAME_AS_PHOTO', '사진과 똑같아요'),
      ('SAME_AS_RESULT', 'BEYOND_EXPECTATION', '기대 이상이에요'),
      ('SAME_AS_RESULT', 'ACCURATE', '정확해요'),
      ('SAME_AS_RESULT', 'BETTER', '더 좋아요'),

      -- TASTE
      ('TASTE', 'DELICIOUS', '맛있어요'),
      ('TASTE', 'FRESH', '신선해요'),
      ('TASTE', 'SWEET', '달콤해요'),
      ('TASTE', 'MOIST', '촉촉해요'),
      ('TASTE', 'NOT_TOO_SWEET', '달지 않아요'),

      -- COMMUNICATION
      ('COMMUNICATION', 'KIND', '친절해요'),
      ('COMMUNICATION', 'DETAILED', '상담이 자세해요'),
      ('COMMUNICATION', 'FAST_RESPONSE', '응답이 빨라요'),
      ('COMMUNICATION', 'GOOD_LISTENER', '요청사항을 잘 들어줘요'),
      ('COMMUNICATION', 'EASY', '소통이 편해요'),

      -- PICKUP
      ('PICKUP', 'EASY_PICKUP', '픽업이 편해요'),
      ('PICKUP', 'CLEAN', '매장이 깨끗해요'),
      ('PICKUP', 'ON_TIME', '시간을 잘 지켜요')
    AS new
ON DUPLICATE KEY UPDATE
                     category = new.category,
                     label = new.label;
